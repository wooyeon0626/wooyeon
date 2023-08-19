package com.wooyeon.yeon.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooyeon.yeon.user.domain.PhoneAuth;
import com.wooyeon.yeon.user.dto.*;
import com.wooyeon.yeon.user.repository.PhoneAuthRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.*;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@PropertySource("classpath:application-apikey.properties")
@Slf4j
@Service
public class SmsAuthService {

    private final PhoneAuthRepository phoneAuthRepository;

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String fromPhone;

    // smsConfirmNum 만료 시간 (40초)
    private static final long EXPIRATION_TIME = 1 * 40 * 1000;

    public SmsAuthService(PhoneAuthRepository phoneAuthRepository) {
        this.phoneAuthRepository=phoneAuthRepository;
    }

    public SmsAuthResponseDto sendSms(PhoneInfoRequestDto phoneInfoRequestDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        // smsDto 설정
        SmsDto smsDto=new SmsDto();
        smsDto.setTo(phoneInfoRequestDto.getTo());

        // 휴대폰 번호 중복 인증 로직
        if(validateDuplicated(smsDto.getTo())){
            SmsAuthResponseDto smsAuthResponseDto= SmsAuthResponseDto.builder()
                    .requestId("duplication")
                    .statusName("duplicated")
                    .statusCode("202")
                    .requestTime(LocalDateTime.now())
                    .build();
            return smsAuthResponseDto;
        }

        //휴대폰 인증 번호 생성
        String smsConfirmNum=createSmsKey();

        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        List<SmsDto> smsDtoList = new ArrayList<>();
        smsDtoList.add(smsDto);

        SmsAuthRequestDto request = SmsAuthRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(fromPhone)
                .content("[우연] 인증번호를 입력해주세요\n"+smsConfirmNum+"\n\n"+phoneInfoRequestDto.getSignature())
                .messages(smsDtoList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsAuthResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ this.serviceId +"/messages"), httpBody, SmsAuthResponseDto.class);

        //phoneAuth에 저장
        LocalDateTime expireDate = LocalDateTime.now().plusSeconds(EXPIRATION_TIME / 1000);
        PhoneAuth phoneAuth = PhoneAuth.builder()
                .phone(smsDto.getTo())
                .verifyCode(smsConfirmNum)
                .expireDate(expireDate)
                .expired(false)
                .build();
        phoneAuthRepository.save(phoneAuth);


        Timer timer = new Timer();
        int delay = 1 * 20 * 1000; // 20초
        int period = 1 * 20 * 1000; // 20초

        timer.schedule(new TimerTask() {

            // TimerTask()는 추상 메서드 run()을 꼭 구현해둬야 함
            public void run() {
                // 여기에 실행하고자 하는 메서드를 호출하면 됩니다.
                deleteExpiredStatusIfExpired();
            }
        }, delay, period);

        return response;
    }

    // 휴대폰 번호 인증 처리
    @Transactional
    public PhoneAuthResponseDto verifyPhone(PhoneAuthRequestDto phoneAuthRequestDto) {
        PhoneAuth phoneAuth= phoneAuthRepository.findByVerifyCode(phoneAuthRequestDto.getVerifyCode())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 코드입니다."));

        if (phoneAuth.isExpired()) {
            throw new IllegalArgumentException("만료된 코드입니다.");
        }

        PhoneAuthResponseDto phoneAuthResponseDto=PhoneAuthResponseDto.builder()
                .phoneAuth("success")
                .profile("none")
                .register("none")
                .serviceTerms("none")
                .build();

        phoneAuth.phoneVerifiedSuccess();
        return phoneAuthResponseDto;
    }

    public String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    // 인증코드 만들기
    public String createSmsKey() {
        int key = ThreadLocalRandom.current().nextInt(999999);
        return Integer.toString(key);
    }

    // 휴대폰 번호 중복 확인 로직 구현
    private boolean validateDuplicated(String phone) {
        // 중복된 휴대폰 번호가 이미 PhoneAuth 테이블에 존재한다면 예외 처리
        if (phoneAuthRepository.existsByPhone(phone)) {
            return true;
        }
        return false;
    }

    // x분 마다 한 번씩 PhoneAuth에 있는 expiredDate가 지난 데이터 삭제
    @Transactional
    public void deleteExpiredStatusIfExpired() {
        LocalDateTime currentDateTime= LocalDateTime.now();
        phoneAuthRepository.deleteExpiredRecords(currentDateTime);
    }

}


package com.wooyeon.yeon.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooyeon.yeon.user.domain.PhoneAuth;
import com.wooyeon.yeon.user.dto.*;
import com.wooyeon.yeon.user.repository.PhoneAuthRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.*;
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@PropertySource("classpath:application.properties")
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

    // smsConfirmNum 만료 시간 (3분)
    private static final long EXPIRATION_TIME = 3 * 60 * 1000;

    public SmsAuthService(PhoneAuthRepository phoneAuthRepository) {
        this.phoneAuthRepository=phoneAuthRepository;
    }

    public SmsAuthResponseDto sendSms(SmsDto smsDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        // 휴대폰 번호 중복 인증 로직
        validateDuplicated(smsDto.getTo());

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
                .content("[우연] 인증번호를 입력해주세요\n"+smsConfirmNum)
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
    private void validateDuplicated(String phone) {
        // 중복된 휴대폰 번호가 이미 PhoneAuth 테이블에 존재한다면 예외 처리
        if (phoneAuthRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("인증코드가 이미 전송되었습니다.");
        }
    }

}


package com.wooyeon.yeon.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooyeon.yeon.user.domain.PhoneAuth;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.*;
import com.wooyeon.yeon.user.repository.PhoneAuthRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@PropertySource("classpath:application-apikey.properties")
@Slf4j
@Service
public class SmsAuthService {

    private final PhoneAuthRepository phoneAuthRepository;
    private final UserRepository userRepository;

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

    public SmsAuthService(PhoneAuthRepository phoneAuthRepository, UserRepository userRepository) {
        this.phoneAuthRepository = phoneAuthRepository;
        this.userRepository = userRepository;
    }

    public SmsAuthResponseDto sendSms(PhoneInfoRequestDto phoneInfoRequestDto) {
        // 인증 코드 만료 시간이 지난 데이터 삭제
        deleteExpiredStatusIfExpired();

        // smsDto 설정
        SmsDto smsDto = new SmsDto();
        smsDto.setTo(phoneInfoRequestDto.getTo());

        String appSignature = phoneInfoRequestDto.getSignature();

        // 휴대폰 번호가 중복일 경우 프론트엔드에게 statusName으로 중복됨을 알려주기(statusCode가 500이 나 버리면 안되기 때문에)
        if (validateDuplicated(smsDto.getTo())) {
            SmsAuthResponseDto smsAuthResponseDto = SmsAuthResponseDto.builder()
                    .requestId("duplication")
                    .statusName("duplicated")
                    .statusCode("202")
                    .requestTime(LocalDateTime.now())
                    .build();
            return smsAuthResponseDto;
        }
        SmsAuthResponseDto smsAuthResponseDto;
        try {
            smsAuthResponseDto = createMessage(smsDto, appSignature);
        } catch (JsonProcessingException | UnsupportedEncodingException | NoSuchAlgorithmException |
                 InvalidKeyException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return smsAuthResponseDto;
    }

    // 네이버 SMS API를 통한 message 생성 및 전송, PhoneAuth에 데이터 저장
    public SmsAuthResponseDto createMessage(SmsDto smsDto, String appSignature) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        //휴대폰 인증 번호 생성
        String smsConfirmNum = createSmsKey();

        Long time = System.currentTimeMillis();

        // 네이버 sms api 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        List<SmsDto> smsDtoList = new ArrayList<>();
        smsDtoList.add(smsDto);

        // 네이버 sms api 이용
        SmsAuthRequestDto request = SmsAuthRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(fromPhone)
                .content("[우연] 인증번호를 입력해주세요\n" + smsConfirmNum + "\n\n" + appSignature)
                .messages(smsDtoList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsAuthResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + this.serviceId + "/messages"), httpBody, SmsAuthResponseDto.class);

        // phoneAuth에 저장
        LocalDateTime expireDate = LocalDateTime.now().plusSeconds(EXPIRATION_TIME / 1000);
        PhoneAuth phoneAuth = PhoneAuth.builder()
                .phone(smsDto.getTo())
                .verifyCode(smsConfirmNum)
                .expireDate(expireDate)
                .build();
        phoneAuthRepository.save(phoneAuth);

        return response;
    }

    // 휴대폰 번호 인증 처리
    @Transactional
    public PhoneAuthResponseDto verifyPhone(PhoneAuthRequestDto phoneAuthRequestDto) {
        PhoneAuthResponseDto phoneAuthResponseDto = null;

        // 휴대폰 번호와 인증 코드가 일치하는 지 확인
        PhoneAuth phoneAuth = phoneAuthRepository.findByPhoneAndVerifyCode(phoneAuthRequestDto.getPhone(), phoneAuthRequestDto.getVerifyCode());
        // 일치하지 않으면 fail 값을 반환
        if(phoneAuth==null) {
            phoneAuthResponseDto = PhoneAuthResponseDto.builder()
                    .phoneAuth("fail")
                    .registerProc(null) // registerProc은 null로 설정
                    .build();
        }

        phoneAuth.phoneVerifiedSuccess(); // 해당 데이터의 certification(인증완료) 값을 true로 설정
        User findUser = userRepository.findUserByPhone(phoneAuthRequestDto.getPhone());

        if(findUser!=null) {
            if(findUser.getAccessToken()!=null) {
                phoneAuthResponseDto = PhoneAuthResponseDto.builder()
                        .phoneAuth("success")
                        .registerProc("register")
                        .build();
            } else if(findUser.getEmail()!=null) {
                phoneAuthResponseDto = PhoneAuthResponseDto.builder()
                        .phoneAuth("success")
                        .registerProc("profile")
                        .build();
            } else if(findUser.getPhone()!=null){
                phoneAuthResponseDto = PhoneAuthResponseDto.builder()
                        .phoneAuth("success")
                        .registerProc("email")
                        .build();
            }
        } else {
            // user 정보에 저장
            User user = User.builder()
                    .phone(phoneAuthRequestDto.getPhone())
                    .userCode(UUID.randomUUID())
                    .build();
            userRepository.save(user);

            phoneAuthResponseDto = PhoneAuthResponseDto.builder()
                    .phoneAuth("success")
                    .build();
        }

        return phoneAuthResponseDto;
    }

    // NAVER SMS API signature 생성
    public String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + this.serviceId + "/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = method +
                space +
                url +
                newLine +
                timestamp +
                newLine +
                accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    // 인증코드 생성
    public String createSmsKey() {
        int key = ThreadLocalRandom.current().nextInt(100000,999999);
        return Integer.toString(key);
    }

    // 휴대폰 번호 중복 확인 로직 구현
    private boolean validateDuplicated(String phone) {
        // 중복된 휴대폰 번호가 이미 PhoneAuth 테이블에 존재한다면 예외 처리
        return phoneAuthRepository.existsByPhone(phone);
    }

    // PhoneAuth에 있는 expiredDate가 지난 데이터 삭제
    @Transactional
    public void deleteExpiredStatusIfExpired() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        phoneAuthRepository.deleteExpiredRecords(currentDateTime);
    }

}


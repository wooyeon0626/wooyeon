package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.EmailAuth;
import com.wooyeon.yeon.user.dto.EmailAuthResponseDto;
import com.wooyeon.yeon.user.dto.EmailRequestDto;
import com.wooyeon.yeon.user.dto.EmailResponseDto;
import com.wooyeon.yeon.user.repository.EmailAuthRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@PropertySource("classpath:application-apikey.properties")
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    // authToken 만료 시간 (10분)
    private static final long EXPIRATION_TIME = 10 * 60 * 1000;

    // 이메일 전송 전 중복 확인, 이메일 전송 메서드 호출
    @Async
    public EmailResponseDto sendEmail(EmailRequestDto emailRequestDto) throws MessagingException {

        // certification이 false이면서(=인증되지 않았으면서) 인증 코드 만료 시간이 지난 데이터 삭제
        deleteExpiredStatusIfExpired();

        EmailResponseDto emailResponseDto;

        // 이메일 중복 확인 로직 추가
        if (validateDuplicated(emailRequestDto.getEmail())) {

            log.info("certification: " + emailAuthRepository.findEmailAuthByEmail(emailRequestDto.getEmail()).isCertification());

            emailResponseDto = EmailResponseDto.builder()
                    .statusCode(HttpStatus.SC_OK) // 오류코드 대신 200 부탁함
                    .email(emailRequestDto.getEmail())
                    .build();


            if (emailAuthRepository.findEmailAuthByEmail(emailRequestDto.getEmail()).isCertification()) {
                emailResponseDto.updateStatusName("completed");
            } else {
                emailResponseDto.updateStatusName("duplicated");
            }

        } else {
            // 이메일 인증 링크 발송
            sendEmailVerification(emailRequestDto);
            emailResponseDto = EmailResponseDto.builder()
                    .statusCode(HttpStatus.SC_ACCEPTED)
                    .email(emailRequestDto.getEmail())
                    .statusName("success")
                    .build();
        }
        return emailResponseDto;
    }

    // authToken 발급 및 이메일 양식 설정, 전송
    public void sendEmailVerification(EmailRequestDto emailRequestDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        // Multipart Message가 필요하므로 true 설정
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String authToken = UUID.randomUUID().toString();
        LocalDateTime expireDate = LocalDateTime.now().plusSeconds(EXPIRATION_TIME / 1000);
        EmailAuth emailAuth = EmailAuth.builder()
                .email(emailRequestDto.getEmail())
                .authToken(authToken)
                .expireDate(expireDate)
                .build();
        emailAuthRepository.save(emailAuth);

        String subject = "우연(WOOYEON) 이메일 인증 링크입니다.";
        message.setSubject(subject);
        helper.setTo(emailRequestDto.getEmail());
        // addInline 보다 먼저 실행 되어야 함
        helper.setText(setContext(emailRequestDto.getEmail() + "&" + authToken), true);
        // addInline을 통해 local에 있는 이미지 삽입 해주기 & html에서 img src='cid:{contentId}'로 설정 해주기
        helper.addInline("wooyeonLogoImage", new ClassPathResource("static/logo_wooyeon_email.png"));

        mailSender.send(message);
    }

    private String setContext(String auth) { // 타임리프 설정하는 코드
        // 이메일+인증토큰 base64로 인코딩
        String url = base64UrlEncode(auth);
        Context context = new Context();
        String link = "http://www.wooyeon-1201.n-e.kr/auth/email/verify?auth=" + url;
//        String link = "http://localhost:8010/auth/email/verify?auth=" + url;
        context.setVariable("link", link); // Template에 전달할 데이터 설정

        log.info("보낸 URL : " + link);
        log.info("보낸 auth : " + url);

        return templateEngine.process("email_authentication", context); // email_authentication.html
    }

    // 이메일 인증 처리
    @Transactional
    public EmailAuthResponseDto verifyEmail(String auth) {
        String decodeUrl = base64UrlDecode(auth);

        // &를 기준으로 문자열 나누기
        String[] parts = decodeUrl.split("&");

        // 나뉜 부분 출력
        String email = parts[0].trim(); // A
        String authToken = parts[1].trim(); // B

        // 결과 출력
        log.info("email:" + email);
        log.info("authToken:" + authToken);

        EmailAuth emailAuth = emailAuthRepository.findEmailAuthByEmailAndAuthToken(email, authToken);
        EmailAuthResponseDto emailAuthResponseDto;

        if (emailAuth != null) {
            emailAuthResponseDto = EmailAuthResponseDto.builder()
                    .emailAuth("success")
                    .email(email)
                    .build();
            emailAuth.emailVerifiedSuccess();

        } else {
            emailAuthResponseDto = EmailAuthResponseDto.builder()
                    .emailAuth("fail")
                    .email(email)
                    .build();
        }
//        emailAuthRepository.deleteByEmail(requestDto.getEmail());
        return emailAuthResponseDto;
    }

    // 이메일 중복 확인 로직 구현
    private boolean validateDuplicated(String email) {
        // 중복된 이메일이 이미 회원 테이블에 존재한다면 예외 처리
        if (emailAuthRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }

    // EmailAuth 있는 expiredDate가 지난 데이터 삭제
    public void deleteExpiredStatusIfExpired() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        emailAuthRepository.deleteExpiredRecords(currentDateTime);
    }

    // BASE64 인코딩
    public String base64UrlEncode(String url) {
        String encodedUrl = Base64.getUrlEncoder().encodeToString(url.getBytes());

        log.info("base64 Encode: " + encodedUrl);

        return encodedUrl;
    }

    // BASE64 디코딩
    public String base64UrlDecode(String auth) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(auth);
        String decodedUrl = new String(decodedBytes);

        log.info("base64 Decode: " + decodedUrl);

        return decodedUrl;
    }
}

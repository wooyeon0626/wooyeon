package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.EmailAuth;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.EmailAuthRequestDto;
import com.wooyeon.yeon.user.dto.EmailAuthResponseDto;
import com.wooyeon.yeon.user.dto.EmailRequestDto;
import com.wooyeon.yeon.user.dto.EmailResponseDto;
import com.wooyeon.yeon.user.repository.EmailAuthRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.UUID;

@PropertySource("classpath:application-apikey.properties")
@Slf4j
@Service
public class EmailAuthService {

    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailAuthService(EmailAuthRepository emailAuthRepository, JavaMailSender mailSender, UserRepository userRepository, SpringTemplateEngine templateEngine) {
        this.emailAuthRepository = emailAuthRepository;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.templateEngine = templateEngine;
    }

    // authToken 만료 시간 (10분)
    private static final long EXPIRATION_TIME = 10 * 60 * 1000;

    // 이메일 전송 전 중복 확인, 이메일 전송 메서드 호출
    @Transactional
    public EmailResponseDto sendEmail(EmailRequestDto emailRequestDto) throws MessagingException {
        // 인증 코드 만료 시간이 지난 데이터 삭제
        deleteExpiredStatusIfExpired();

        EmailResponseDto emailResponseDto;

        // 이메일 중복 확인 로직 추가
        if (validateDuplicated(emailRequestDto.getEmail())) {

            emailResponseDto = EmailResponseDto.builder()
                    .statusCode(HttpStatus.SC_OK) // 오류코드 대신 200 부탁함
                    .statusName("duplicated")
                    .email(emailRequestDto.getEmail())
                    .build();
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
        MimeMessageHelper helper=new MimeMessageHelper(message,true);

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
        helper.setText(setContext(emailRequestDto.getEmail()),true);
        // addInline을 통해 local에 있는 이미지 삽입 해주기 & html에서 img src='cid:{contentId}'로 설정 해주기
        helper.addInline("wooyeonLogoImage",new ClassPathResource("static/logo_wooyeon_email.png"));

        mailSender.send(message);
    }

    private String setContext(String email) { // 타임리프 설정하는 코드
        Context context = new Context();
        String link = "https://our-audio-394406.du.r.appspot.com/redirect?auth=" + email;
        context.setVariable("link", link); // Template에 전달할 데이터 설정
        return templateEngine.process("email_authentication", context); // email_authentication.html
    }

    // 이메일 인증 처리
    @Transactional
    public EmailAuthResponseDto verifyEmail(EmailAuthRequestDto emailAuthRequestDto) {
        EmailAuth emailAuth = emailAuthRepository.findByEmailAndAuthToken(emailAuthRequestDto.getEmail(), emailAuthRequestDto.getAuthToken());
        EmailAuthResponseDto emailAuthResponseDto;

        if (emailAuth != null) {
            emailAuthResponseDto = EmailAuthResponseDto.builder()
                    .emailAuth("success")
                    .build();
            emailAuth.emailVerifiedSuccess();

            User user = userRepository.findByPhone(emailAuthRequestDto.getPhone());
            user.updateEmail(emailAuthRequestDto.getEmail());
            userRepository.save(user);

        } else {
            emailAuthResponseDto = EmailAuthResponseDto.builder()
                    .emailAuth("fail")
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
    @Transactional
    public void deleteExpiredStatusIfExpired() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        emailAuthRepository.deleteExpiredRecords(currentDateTime);
    }

    public String findAuthTokneByEmail(String email) {
        EmailAuth emailAuth = emailAuthRepository.findAuthTokenByEmail(email);
        String authToken = emailAuth.getAuthToken();

        return authToken;
    }
}

package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.EmailAuth;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.EmailAuthRequestDto;
import com.wooyeon.yeon.user.dto.EmailAuthResponseDto;
import com.wooyeon.yeon.user.dto.EmailDto;
import com.wooyeon.yeon.user.repository.EmailAuthRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

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
        this.userRepository=userRepository;
        this.templateEngine = templateEngine;
    }

    // authToken 만료 시간 (5분)
    private static final long EXPIRATION_TIME = 10 * 60 * 1000;

    // authToken 발급 및 이메일 전송
    public void sendEmailVerification(User user) throws MessagingException {
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true,"utf-8");

        String authToken = UUID.randomUUID().toString();
        LocalDateTime expireDate = LocalDateTime.now().plusSeconds(EXPIRATION_TIME / 1000);
        EmailAuth emailAuth = EmailAuth.builder()
                .email(user.getEmail())
                .authToken(authToken)
                .expireDate(expireDate)
                .expired(false)
                .build();
        emailAuthRepository.save(emailAuth);

        String subject = "우연(WOOYEON) 이메일 인증 링크입니다.";
        message.addRecipients(MimeMessage.RecipientType.TO, user.getEmail());
        message.setSubject(subject);
        message.setText(setContext(authToken),"utf-8", "html");

        helper.addInline("wooyeonLogoImage",new ClassPathResource("static/logo_wooyeon_email.png"));

        mailSender.send(message);
    }

    private String setContext(String authToken) { // 타임리프 설정하는 코드
        Context context = new Context();
        String link="wooyeon://email_auth?token="+authToken;
        context.setVariable("link", link); // Template에 전달할 데이터 설정
        return templateEngine.process("email_authentication", context); // email_authentication.html
    }

    // 이메일 인증 처리
    @Transactional
    public void verifyEmail(EmailAuthRequestDto requestDto) {
        EmailAuth emailAuth = emailAuthRepository.findByAuthToken(requestDto.getAuthToken())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (emailAuth.isExpired()) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        }

        emailAuth.emailVerifiedSuccess();
        System.out.println(requestDto.getEmail());
//        emailAuthRepository.deleteByEmail(requestDto.getEmail());
    }

    @Transactional
    public EmailAuthResponseDto sendEmail(EmailDto emailDto) throws MessagingException {
        // 이메일 중복 확인 로직 추가
        validateDuplicated(emailDto.getEmail());

        // 회원 정보 저장
        User user = User.builder()
                .email(emailDto.getEmail())
                .build();
        userRepository.save(user);

        // 이메일 인증 링크 발송
        sendEmailVerification(user);

        // 회원 가입 응답 생성
        return EmailAuthResponseDto.builder()
                .email(user.getEmail())
                .authToken(null) // 여기에는 authToken이 아직 없으므로 null로 설정
                .build();
    }

    // 이메일 중복 확인 로직 구현
    private void validateDuplicated(String email) {
        // 중복된 이메일이 이미 회원 테이블에 존재한다면 예외 처리
        if (emailAuthRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }
    }
}

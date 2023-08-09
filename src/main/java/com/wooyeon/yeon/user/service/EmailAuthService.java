package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.EmailAuth;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.EmailAuthRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class EmailAuthService {

    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailAuthService(EmailAuthRepository emailAuthRepository, JavaMailSender mailSender) {
        this.emailAuthRepository = emailAuthRepository;
        this.mailSender = mailSender;
    }

    // authToken 만료 시간 (5분)
    private static final long EXPIRATION_TIME = 10 * 60 * 1000;

    // authToken 발급 및 이메일 전송
    public void sendEmailVerification(User user) {
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
        //link가 이러면 post인 이유가...?? -> 나중에 프론트엔드와 상의하여 변경
        String link = "wooyeon://email_auth?token="+ authToken;
        String text = "아래 링크를 클릭하여 이메일 인증을 완료하세요.\n" + link;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    // 이메일 인증 처리
    @Transactional
    public void verifyEmail(String email, String authToken) {
        EmailAuth emailAuth = emailAuthRepository.findByAuthToken(authToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (emailAuth.isExpired()) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        }

        emailAuth.emailVerifiedSuccess();
        System.out.println(email);
        emailAuthRepository.deleteByEmail(email);
    }
}

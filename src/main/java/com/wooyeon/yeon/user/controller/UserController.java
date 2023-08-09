package com.wooyeon.yeon.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wooyeon.yeon.user.dto.EmailAuthRequestDto;
import com.wooyeon.yeon.user.dto.EmailAuthResponseDto;
import com.wooyeon.yeon.user.dto.SmsDto;
import com.wooyeon.yeon.user.dto.SmsResponseDto;
import com.wooyeon.yeon.user.service.EmailAuthService;
import com.wooyeon.yeon.user.service.SmsService;
import com.wooyeon.yeon.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;
    private final SmsService smsService;

    @Autowired
    public UserController(UserService userService, EmailAuthService emailAuthService, SmsService smsService) {
        this.userService = userService;
        this.emailAuthService = emailAuthService;
        this.smsService = smsService;
    }

    @PostMapping(value = "/auth/email",produces = "application/json;charset=UTF-8")
    public ResponseEntity<EmailAuthResponseDto> registerMember(String email) {
        EmailAuthRequestDto requestDto = new EmailAuthRequestDto();
        requestDto.setEmail(email);
        EmailAuthResponseDto responseDto = userService.registerMember(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(value = "/auth/email/verify",produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, String token) {
        emailAuthService.verifyEmail(email, token);
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }

    @PostMapping(value = "/auth/phone",produces = "application/json;charset=UTF-8")
    public SmsResponseDto sendSms(@RequestBody String phone) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        SmsResponseDto responseDto = smsService.sendSms(phone);
        return responseDto;
    }

}
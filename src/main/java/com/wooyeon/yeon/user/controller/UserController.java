package com.wooyeon.yeon.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wooyeon.yeon.user.dto.*;
import com.wooyeon.yeon.user.service.EmailAuthService;
import com.wooyeon.yeon.user.service.SmsAuthService;
import com.wooyeon.yeon.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class UserController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;
    private final SmsAuthService smsAuthService;

    public UserController(UserService userService, EmailAuthService emailAuthService, SmsAuthService smsAuthService) {
        this.userService = userService;
        this.emailAuthService = emailAuthService;
        this.smsAuthService = smsAuthService;
    }

    @PostMapping(value = "/auth/email",produces = "application/json;charset=UTF-8")
    public ResponseEntity<EmailAuthResponseDto> sendEmailVerify(@RequestBody EmailDto emailDto) {
        EmailAuthResponseDto responseDto = emailAuthService.sendEmail(emailDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(value = "/auth/email/verify",produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailAuthRequestDto requestDto) {
        emailAuthService.verifyEmail(requestDto);
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }

    @PostMapping(value = "/auth/phone",produces = "application/json;charset=UTF-8")
    public ResponseEntity<SmsAuthResponseDto> sendSmsVerify(@RequestBody SmsDto smsDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        SmsAuthResponseDto responseDto = smsAuthService.sendSms(smsDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping(value = "/auth/phone/verify",produces = "application/json;charset=UTF-8")
    public ResponseEntity<PhoneAuthResponseDto> verifyPhone(@RequestBody PhoneAuthRequestDto phoneAuthRequestDto) {
        PhoneAuthResponseDto responseDto=smsAuthService.verifyPhone(phoneAuthRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

}
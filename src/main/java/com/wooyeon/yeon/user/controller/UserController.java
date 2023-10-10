package com.wooyeon.yeon.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wooyeon.yeon.user.dto.*;
import com.wooyeon.yeon.user.service.EmailAuthService;
import com.wooyeon.yeon.user.service.ProfileService;
import com.wooyeon.yeon.user.service.SmsAuthService;
import com.wooyeon.yeon.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;
    private final SmsAuthService smsAuthService;
    private final ProfileService profileService;

    // 사용자의 이메일로 인증 메일 전송
    public UserController(UserService userService, EmailAuthService emailAuthService, SmsAuthService smsAuthService, ProfileService profileService) {
        this.userService = userService;
        this.emailAuthService = emailAuthService;
        this.smsAuthService = smsAuthService;
        this.profileService = profileService;
    }

    // 사용자의 휴대폰으로 인증번호 전송
    @PostMapping(value = "/auth/phone", produces = "application/json;charset=UTF-8")
    public ResponseEntity<SmsAuthResponseDto> sendSmsVerify(@RequestBody PhoneInfoRequestDto phoneInfoRequestDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        SmsAuthResponseDto responseDto = smsAuthService.sendSms(phoneInfoRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    // 인증번호 확인
    @PostMapping(value = "/auth/phone/verify", produces = "application/json;charset=UTF-8")
    public ResponseEntity<PhoneAuthResponseDto> verifyPhone(@RequestBody PhoneAuthRequestDto phoneAuthRequestDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        PhoneAuthResponseDto responseDto = smsAuthService.verifyPhone(phoneAuthRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    // 사용자의 이메일에 있는 토큰 번호로 인증
    @PostMapping(value = "/auth/email", produces = "application/json;charset=UTF-8")
    public ResponseEntity<EmailResponseDto> sendEmailVerify(@RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        EmailResponseDto responseDto = emailAuthService.sendEmail(emailRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(value = "/auth/email/verify", produces = "application/json;charset=UTF-8")
    public ResponseEntity<EmailAuthResponseDto> verifyEmail(@RequestBody EmailAuthRequestDto requestDto) {
        EmailAuthResponseDto emailAuthResponseDto = emailAuthService.verifyEmail(requestDto);
        return ResponseEntity.ok(emailAuthResponseDto);
    }

    // 앱 링크로 redirect 하기 위한 api
    @GetMapping("/redirect")
    public void redirectToDeepLink(@RequestParam String auth, HttpServletResponse response) {
        String email = auth;
        String token = emailAuthService.findAuthTokenByEmail(email);
        String redirectUrl = "wooyeon://email_auth?token=" + token;
        response.setHeader("Location", redirectUrl);
        response.setStatus(302);
    }

    // 프로필 등록
    @PostMapping(value = "/users/register/profile")
    public ResponseEntity<ProfileResponseDto> insertProfile(@RequestPart(value = "profileInfo") ProfileRequestDto profileRequestDto,
                                                            @RequestPart(value = "profilePhoto", required = false) List<MultipartFile> profilePhotoUpload) throws IOException {
        ProfileResponseDto profileResponseDto = profileService.insertProfile(profileRequestDto, profilePhotoUpload);
        return ResponseEntity.ok(profileResponseDto);
    }

}
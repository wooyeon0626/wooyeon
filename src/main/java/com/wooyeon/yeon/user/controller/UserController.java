package com.wooyeon.yeon.user.controller;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.*;
import com.wooyeon.yeon.user.dto.emailAuth.EmailAuthResponseDto;
import com.wooyeon.yeon.user.dto.emailAuth.EmailRequestDto;
import com.wooyeon.yeon.user.dto.emailAuth.EmailResponseDto;
import com.wooyeon.yeon.user.service.EmailAuthService;
import com.wooyeon.yeon.user.service.LoginService;
import com.wooyeon.yeon.user.service.ProfileService;
import com.wooyeon.yeon.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;
    private final ProfileService profileService;
    private final LoginService loginService;
    private final Map<String, SseEmitter> userEmitters = new ConcurrentHashMap<>();

    @Value("${email-auth-background-image}")
    private String emailAuthBackgroundImg;

    // 사용자에게 인증메일 전송 및 프론트엔드와 SSE 연결
    @PostMapping(value = "/auth/email", produces = "application/json;charset=UTF-8")
    public SseEmitter sendEmailVerify(@RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        userEmitters.put(emailRequestDto.getEmail(), emitter);

        EmailResponseDto emailResponseDto = emailAuthService.sendEmail(emailRequestDto);
        log.debug("userEmitter: {}", userEmitters);

        // SSE 연결 여부 메시지 전송
        try {
            emitter.send(SseEmitter.event().name("INIT").data("SSE Connected"));
            emitter.send(SseEmitter.event().data(emailResponseDto));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        log.debug("SSE MSG : " + emitter);

        return emitter;
    }

    // 사용자의 이메일 인증 (ModelAndView로 인증완료 페이지 html 보여주기)
    @GetMapping(value = "/auth/email/verify")
    public ModelAndView verifyEmail(@RequestParam String auth) {

        EmailAuthResponseDto emailAuthResponseDto = emailAuthService.verifyEmail(auth);
        sendSseEmitter(emailAuthResponseDto);

        log.info("verify request : " + auth);
        log.info("verify 프론트에게 : " + emailAuthResponseDto);

        ModelAndView mv = new ModelAndView("email_auth_verify");
        mv.addObject("backgroundImg", emailAuthBackgroundImg);

        return mv;
    }

    // RSA 공개키 전송
    @GetMapping("/encrypt/key")
    public RsaPublicResponseDto sendRsaPublicKey() {
        RsaPublicResponseDto rsaPublicResponseDto = userService.sendRsaPublicKey();

        return rsaPublicResponseDto;
    }

    // 암호화된 비밀번호와 RSA 공개키로 암호화된 AES 복호화 키 전달
    @PostMapping("/encrypt/pw")
    public PasswordEncryptResponseDto passwordEncrypt(@RequestBody PasswordEncryptRequestDto passwordEncryptRequestDto)
            throws Exception {
        PasswordEncryptResponseDto passwordEncryptResponseDto = userService.savePassword(passwordEncryptRequestDto);
        return passwordEncryptResponseDto;
    }

    // 프로필 등록
    @PostMapping(value = "/users/register/profile")
    public ResponseEntity<ProfileResponseDto> createProfile(@RequestPart(value = "profileInfo") ProfileRequestDto profileRequestDto,
                                                            @RequestPart(value = "profilePhoto", required = false) List<MultipartFile> profilePhotoUpload) throws IOException {
        ProfileResponseDto profileResponseDto = profileService.insertProfile(profileRequestDto, profilePhotoUpload);
        return ResponseEntity.ok(profileResponseDto);
    }

    // accessToken 검증
    @GetMapping("/users/profile-state")
    public ResponseEntity<ExpiredCheckDto.ExpiredCheckResponse> checkExpiredTokenAndProfile(HttpServletRequest request) {
        String accessToken = parseBearerToken(request);
        log.info("accessToken : {}", accessToken);
        return ResponseEntity.ok(loginService.checkTokenAndProfile(accessToken));
    }

    // GPS 수신 API
    @PostMapping("/users/profile/gps")
    public ResponseEntity<HttpStatus> receiveUsersGps(@AuthenticationPrincipal User user,
                                                      @RequestBody String gpsLocation) {
//        String accessToken = parseBearerToken(request);
        log.debug("user 정보 :{}", user);
        return ResponseEntity.ok(profileService.updateUsersGpsLocation(user, gpsLocation));
    }

    // 이메일 인증 시, 프론트엔드에게 SSE emitter로 인증완료 전송
    public SseEmitter sendSseEmitter(EmailAuthResponseDto emailAuthResponseDto) {
        SseEmitter emitter = userEmitters.get(emailAuthResponseDto.getEmail());

        log.info("SSE EMITTER(VERIFY) : " + emitter);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("VERIFY").data(emailAuthResponseDto));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }

        emitter.onCompletion(() -> userEmitters.remove(emitter));
        emitter.onTimeout(() -> userEmitters.remove(emitter));

        return emitter;
    }

    private String parseBearerToken(HttpServletRequest request) {
        // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
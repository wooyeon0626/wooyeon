package com.wooyeon.yeon.user.controller;

import com.wooyeon.yeon.user.dto.*;
import com.wooyeon.yeon.user.service.EmailAuthService;
import com.wooyeon.yeon.user.service.ProfileService;
import com.wooyeon.yeon.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.mail.MessagingException;
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
    private final Map<String, SseEmitter> userEmitters = new ConcurrentHashMap<>();


    // 사용자의 이메일에 있는 토큰 번호로 인증
    @PostMapping(value = "/auth/email")
    public SseEmitter sendEmailVerify(@RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        userEmitters.put(emailRequestDto.getEmail(), emitter);

        EmailResponseDto emailResponseDto = emailAuthService.sendEmail(emailRequestDto);

        log.info("userEmitter: "+userEmitters);

        // SSE 연결 여부 메시지 전송
        try {
            emitter.send(SseEmitter.event().name("INIT").data("SSE Connected"));
            emitter.send(SseEmitter.event().data(emailResponseDto));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        log.info("SSE MSG : "+emitter);

        emitter.onCompletion(() -> userEmitters.remove(emitter));
        emitter.onTimeout(() -> userEmitters.remove(emitter));

        return emitter;
    }


    @PostMapping(value = "/auth/email/verify")
    public SseEmitter verifyEmail(@RequestBody EmailAuthRequestDto emailAuthRequestDto) {

        EmailAuthResponseDto emailAuthResponseDto = emailAuthService.verifyEmail(emailAuthRequestDto);
        SseEmitter emitter = userEmitters.get(emailAuthResponseDto.getEmail());

        log.info("SSE EMITTER(VERIFY) : "+emitter);
        log.info("verify request : "+emailAuthRequestDto);
        log.info("verify 프론트에게 : "+emailAuthResponseDto);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("VERIFY").data(emailAuthResponseDto));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
        return emitter;
    }

    // 프로필 등록
    @PostMapping(value = "/users/register/profile")
    public ResponseEntity<ProfileResponseDto> insertProfile(@RequestPart(value = "profileInfo") ProfileRequestDto profileRequestDto,
                                                            @RequestPart(value = "profilePhoto", required = false) List<MultipartFile> profilePhotoUpload) throws IOException {
        ProfileResponseDto profileResponseDto = profileService.insertProfile(profileRequestDto, profilePhotoUpload);
        return ResponseEntity.ok(profileResponseDto);
    }

}
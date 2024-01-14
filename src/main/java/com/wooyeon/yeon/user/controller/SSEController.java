package com.wooyeon.yeon.user.controller;

import com.wooyeon.yeon.user.dto.EmailRequestDto;
import com.wooyeon.yeon.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SSEController {
    private final UserService userService;

    /*
    private final Map<String, SseEmitter> userEmitters = new ConcurrentHashMap<>();

    @PostMapping("/connect")
    public SseEmitter subscribe(@RequestBody EmailRequestDto emailRequestDto) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        userEmitters.put(emailRequestDto.getEmail(), emitter);

        // SSE 연결 여부 메시지 전송
        try {
            emitter.send(SseEmitter.event().name("INIT").data("SSE Connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        emitter.onCompletion(() -> userEmitters.remove(emitter));
        emitter.onTimeout(() -> userEmitters.remove(emitter));

        return emitter;
    }


    // Method to send string updates to clients
    @PostMapping("/connect/new")
    public void sendStringUpdateToClients(String data) {
        SseEmitter emitter = userEmitters.get(email);

        // 비즈니스 로직 수행
        String result = "performBusinessLogic(email)";

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("VERIFY").data(result));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }

    }

     */

}
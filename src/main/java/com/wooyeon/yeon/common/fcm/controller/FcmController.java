package com.wooyeon.yeon.common.fcm.controller;

import com.wooyeon.yeon.common.fcm.dto.FcmDto;
import com.wooyeon.yeon.common.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;

    @GetMapping("/get/token")
    public String getAccessToken() throws IOException {
        return fcmService.getAccessToken();
    }

    @PostMapping("/notification")
    public void sendNotificationByToken(@RequestBody FcmDto.Request request) throws IOException {
        fcmService.sendMessageTo(request);
    }

    @PostMapping("/save")
    public com.wooyeon.yeon.chat.dto.FcmDto.Response saveFcmToken(com.wooyeon.yeon.chat.dto.FcmDto.Request request) {
        return fcmService.saveFcmToken(request);
    }
}

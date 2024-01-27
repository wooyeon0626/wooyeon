package com.wooyeon.yeon.common.fcm.controller;

import com.wooyeon.yeon.common.fcm.dto.FCMNotificationRequestDto;
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
    public String sendNotificationByToken(@RequestBody FCMNotificationRequestDto requestDto) {
        return fcmService.sendNotificationByToken(requestDto);
    }
}

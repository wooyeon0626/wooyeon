package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.ChatUserDto;
import com.wooyeon.yeon.chat.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ChatUserController {

    private final ChatUserService chatUserService;

    @PostMapping("/block")
    public void blockUser(ChatUserDto.ChatUserRequest chatUserRequest) {
        chatUserService.blockUser(chatUserRequest);
    }

    @PostMapping("/report")
    public void reportUser(ChatUserDto.ChatUserRequest chatUserRequest) {
        chatUserService.reportUser(chatUserRequest);
    }
}

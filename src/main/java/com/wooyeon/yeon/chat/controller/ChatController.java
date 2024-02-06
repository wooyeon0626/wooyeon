package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.ChatDto;
import com.wooyeon.yeon.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/list")
    public ChatDto.Response getChatList(@RequestParam Long matchId) {
        return chatService.getChatList(matchId);
    }
}

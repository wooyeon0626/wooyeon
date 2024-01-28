package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.ChatDto;
import com.wooyeon.yeon.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private ChatService chatService;

    @PostMapping("/list")
    public List<ChatDto.Response> getChatList(
            @Valid @RequestBody ChatDto.Request request) {
        return chatService.getChatList(request);
    }
}

package com.wooyeon.yeon.chat.controller;

import com.wooyeon.yeon.chat.dto.RoomDto;
import com.wooyeon.yeon.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private ChatService chatService;
}

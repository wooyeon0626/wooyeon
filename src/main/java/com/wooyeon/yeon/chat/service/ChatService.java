package com.wooyeon.yeon.chat.service;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.chat.dto.StompDto;
import com.wooyeon.yeon.chat.repository.ChatRepository;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MatchRepository matchRepository;

    public void saveChat(StompDto stompDto) {
        UserMatch userMatch = matchRepository.findById(stompDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException());

        Chat chat = Chat.builder()
                .message(stompDto.getMessage())
                .sendTime(LocalDateTime.now())
                .userMatch(userMatch)
                .build();

        chatRepository.save(chat);
    }
}

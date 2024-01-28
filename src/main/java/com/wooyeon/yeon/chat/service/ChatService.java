package com.wooyeon.yeon.chat.service;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.chat.dto.ChatDto;
import com.wooyeon.yeon.chat.dto.StompDto;
import com.wooyeon.yeon.chat.repository.ChatRepository;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MatchRepository matchRepository;

    @Transactional
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

    public List<ChatDto.Response> getChatList(ChatDto.Request request) {
        Long matchId = request.getMatchId();

        return chatRepository.findAllByUserMatchIdOOrderBySendTime(matchId)
                .stream()
                .map(this::makeResponse)
                .collect(Collectors.toList());
    }

    public ChatDto.Response makeResponse(Chat chat) {
        return ChatDto.Response.builder()
                .message(chat.getMessage())
                .sender(chat.getSender())
                .sendTime(chat.getSendTime())
                .isChecked(chat.isChecked())
                .build();
    }
}

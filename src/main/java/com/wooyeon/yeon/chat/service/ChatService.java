package com.wooyeon.yeon.chat.service;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.chat.dto.StompDto;
import com.wooyeon.yeon.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    // 클라이언트에서 전달받은 chatDto(메시지 정보) DB에 저장
    public void insertChat(StompDto stompDto) {
//        Long matchId = chatDto.getMatchId();
//        Match match = new Match(); matchId로 Match 객체 조회 해야 함.
        String sender = stompDto.getSender();
        String message = stompDto.getMessage();

        chatRepository.save(Chat.builder()
                .message(message)
                .sender(sender)
//                .match(match)
                .build());
    }
}

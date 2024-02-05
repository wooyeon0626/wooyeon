package com.wooyeon.yeon.chat.service;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.chat.dto.ChatDto;
import com.wooyeon.yeon.chat.dto.StompDto;
import com.wooyeon.yeon.chat.repository.ChatRepository;
import com.wooyeon.yeon.common.security.SecurityService;
import com.wooyeon.yeon.exception.ExceptionMessage;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.ProfileRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MatchRepository matchRepository;
    private final SecurityService securityService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Transactional
    public void saveChat(StompDto stompDto) {
        UserMatch userMatch = matchRepository.findById(stompDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.USER_MATCH_NOT_FOUND.toString()));

        Chat chat = Chat.builder()
                .message(stompDto.getMessage())
                .sendTime(LocalDateTime.now())
                .userMatch(userMatch)
                .sender(getLoginUserNickName())
//                .isChecked() //stomp 연결되어 있으면 check
                .build();

        chatRepository.save(chat);
    }

    public List<ChatDto.Response> getChatList(Long matchId) {

        UserMatch userMatch = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.USER_MATCH_NOT_FOUND.toString()));

        List<Chat> chatList = chatRepository.findAllByUserMatchOrderBySendTime(userMatch);
        List<ChatDto.Response> responseList = new ArrayList<>();

        String userName = getLoginUserNickName();

        for (Chat chat : chatList) {
            responseList.add(makeResponse(chat, userName));
        }

        return responseList;
    }

    public ChatDto.Response makeResponse(Chat chat, String userName) {
        return ChatDto.Response.builder()
                .message(chat.getMessage())
                .sender(chat.getSender())
                .sendTime(chat.getSendTime())
                .isChecked(chat.isChecked())
                .isSender(chat.getSender().equals(userName))
                .build();
    }

    public String getLoginUserNickName() {
        User loginUser = userRepository.findOptionalByEmail(securityService.getCurrentUserEmail())
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.LOGIN_USER_NOT_FOUND.toString()));

        Optional<Profile> profile;
        String loginUserNickname = null;

        if (null != loginUser.getUserProfile()) {
            profile = profileRepository.findById(loginUser.getUserProfile().getId());
            loginUserNickname = profile.get().getNickname();
        }

        return loginUserNickname;
    }

    public int calculateUserCount() {
        // 중복된 사용자를 고려하여 사용자 수를 계산
        return (int) sessions.stream().map(s -> s.getAttributes().get("userId")).distinct().count();
    }

}

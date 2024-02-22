package com.wooyeon.yeon.chat.service;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.chat.dto.ChatDto;
import com.wooyeon.yeon.chat.dto.StompDto;
import com.wooyeon.yeon.chat.repository.ChatRepository;
import com.wooyeon.yeon.common.security.SecurityService;
import com.wooyeon.yeon.exception.ExceptionCode;
import com.wooyeon.yeon.exception.WooyeonException;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.ProfileRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MatchRepository matchRepository;
    private final SecurityService securityService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public void saveChat(StompDto stompDto, Map<String, String> sessionStore) {
        UserMatch userMatch = matchRepository.findById(stompDto.getRoomId())
                .orElseThrow(() -> new WooyeonException(ExceptionCode.USER_MATCH_NOT_FOUND));

        boolean flag = false;

        if("2".equals(sessionStore.get(stompDto.getRoomId().toString()))) {
            flag = true;
        }

        Chat chat = Chat.builder()
                .message(stompDto.getMessage())
                .sendTime(LocalDateTime.now())
                .userMatch(userMatch)
                .sender(getLoginUserNickName())
                .isChecked(flag) //stomp 연결되어 있으면 check
                .build();

        chatRepository.save(chat);
    }

    public ChatDto.Response getChatList(Long matchId) {

        UserMatch userMatch = matchRepository.findById(matchId)
                .orElseThrow(() -> new WooyeonException(ExceptionCode.USER_MATCH_NOT_FOUND));

        List<Chat> chatList = chatRepository.findAllByUserMatchOrderBySendTime(userMatch);
        List<ChatDto.ChatResponse> responseList = new ArrayList<>();

        String userName = getLoginUserNickName();

        for (Chat chat : chatList) {
            responseList.add(makeResponse(chat, userName));
        }

        return ChatDto.Response.builder()
                .chatData(responseList)
                .build();
    }

    public ChatDto.ChatResponse makeResponse(Chat chat, String userName) {
        return ChatDto.ChatResponse.builder()
                .message(chat.getMessage())
                .sender(chat.getSender())
                .sendTime(chat.getSendTime())
                .isChecked(chat.isChecked())
                .isSender(chat.getSender().equals(userName))
                .build();
    }

    public String getLoginUserNickName() {
        User loginUser = userRepository.findOptionalByEmail("young1@naver.com")
                .orElseThrow(() -> new WooyeonException(ExceptionCode.LOGIN_USER_NOT_FOUND));

        Optional<Profile> profile;
        String loginUserNickname = null;

        if (null != loginUser.getUserProfile()) {
            profile = profileRepository.findById(loginUser.getUserProfile().getId());
            loginUserNickname = profile.get().getNickname();
        }

        return loginUserNickname;
    }
}

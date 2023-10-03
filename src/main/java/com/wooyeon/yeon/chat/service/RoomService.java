package com.wooyeon.yeon.chat.service;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.chat.dto.RoomDto;
import com.wooyeon.yeon.chat.repository.ChatRepository;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.ProfilePhoto;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.ProfilePhotoRepository;
import com.wooyeon.yeon.user.repository.ProfileRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final ProfileRepository profileRepository;
    private final ChatRepository chatRepository;

    public List<RoomDto.RoomResponse> matchRoomList() {
        List<RoomDto.RoomResponse> roomList = new ArrayList();

        Long userId = 1l;
        User user = userRepository.findByUserId(userId);

        List<UserMatch> userMatches = matchRepository.findAllByUserLike1(user)
                .orElseThrow(() -> new IllegalArgumentException());

        for (UserMatch userMatch : userMatches) {
            Long matchId = userMatch.getMatchId();

            Profile profile = profileRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException());

            ProfilePhoto profilePhoto = profilePhotoRepository.findAllByProfileId(profile.getId())
                    .orElseThrow(() -> new IllegalArgumentException());

            Chat lastChatInfo = chatRepository.findFirstByUserMatchIdDOrderBySendTimeDesc(matchId);

            RoomDto.RoomResponse response = RoomDto.RoomResponse.builder()
                    .matchId(matchId)
                    .profilePhoto(profilePhoto.getProfilePhotoId())
                    .name(profile.getNickname())
                    .lastTime(lastChatInfo.getSendTime())
                    .lastMessage(lastChatInfo.getMessage())
                    .build();

            roomList.add(response);
        }
        return roomList;
    }
}

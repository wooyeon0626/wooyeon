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
import java.util.Optional;
import java.util.Set;

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

        List<UserMatch> userMatches = matchRepository.findAllByUser1(userRepository.findByUserId(1l))
                .orElseThrow(() -> new IllegalArgumentException());

        for (UserMatch userMatch : userMatches) {

            // 상대방 프로필 정보 조회
            Profile profile = profileRepository.findById(userMatch.getUser2().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException());

            Optional<ProfilePhoto> profilePhoto = profilePhotoRepository.findByProfileId(profile.getId());

            Chat lastChatInfo = chatRepository.findFirstByUserMatchOrderBySendTimeDesc(userMatch);

            RoomDto.RoomResponse response = RoomDto.RoomResponse.builder()
                    .matchId(userMatch.getMatchId())
                    .profilePhoto(profilePhoto.get().getProfilePhotoId())
                    .name(profile.getNickname())
                    .lastTime(lastChatInfo.getSendTime())
                    .lastMessage(lastChatInfo.getMessage())
                    .build();

            roomList.add(response);
        }
        return roomList;
    }

    public Set<RoomDto.SearchRoomResponse> searchMatchRoomList(RoomDto.SearchRoomRequest request) {
        Set<RoomDto.SearchRoomResponse> searchRoomList = null;

        // 채팅방 내 검색 단어 포함 항목 조회 후 추가
        List<Chat> chatList = chatRepository.findAllByMessageContains(request.getSearchWord());
        for (Chat chat : chatList) {
            UserMatch userMatch = chat.getUserMatch();

            Optional<Profile> profile = profileRepository.findById(userMatch.getUser2().getUserId());
            Optional<ProfilePhoto> profilePhoto = profilePhotoRepository.findByProfileId(profile.get().getId());

            RoomDto.SearchRoomResponse response = RoomDto.SearchRoomResponse.builder()
                    .matchId(userMatch.getMatchId())
                    .profilePhoto(profilePhoto.get().getProfilePhotoId())
                    .name(profile.get().getNickname())
                    .build();

            searchRoomList.add(response);
        }

        // 이름이 같은 사람 조회 후 추가
        List<UserMatch> userMatches = matchRepository.findAllByUser1(userRepository.findByUserId(1l))
                .orElseThrow(() -> new IllegalArgumentException());

        for (UserMatch userMatch : userMatches) {
            Long matchId = userMatch.getMatchId();

            Optional<Profile> profile = profileRepository.findByNicknameContains(request.getSearchWord());
            Optional<ProfilePhoto> profilePhoto = profilePhotoRepository.findByProfileId(profile.get().getId());

            if(profile.isPresent()) {
                RoomDto.SearchRoomResponse response = RoomDto.SearchRoomResponse.builder()
                        .matchId(matchId)
                        .profilePhoto(profilePhoto.get().getProfilePhotoId())
                        .name(profile.get().getNickname())
                        .build();

                searchRoomList.add(response);
            }
        }

        return searchRoomList;
    }
}
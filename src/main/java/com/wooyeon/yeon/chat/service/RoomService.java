package com.wooyeon.yeon.chat.service;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.chat.dto.RoomDto;
import com.wooyeon.yeon.chat.repository.ChatRepository;
import com.wooyeon.yeon.common.security.SecurityService;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final ProfileRepository profileRepository;
    private final ChatRepository chatRepository;
    private final SecurityService service;

    public List<RoomDto.RoomResponse> matchRoomList() {

        User loginUser = userRepository.findOptionalByEmail(service.getCurrentUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        List<UserMatch> userMatchList = matchRepository.findAllByUser1OrUser2(loginUser, loginUser);

        List<RoomDto.RoomResponse> roomList = new ArrayList();

        if (0 < userMatchList.size()) {
            for (UserMatch userMatch : userMatchList) {
                Long matchUserId = getMatchUserId(userMatch, loginUser);

                // 상대방 프로필 정보 조회
                Profile profile = profileRepository.findById(
                                userRepository.findByUserId(matchUserId).getUserProfile().getId())
                        .orElseThrow(() -> new IllegalArgumentException("User Profile does not exist"));

                Optional<ProfilePhoto> profilePhoto = profilePhotoRepository.findByProfileId(profile.getId());

                Optional<Chat> lastChatInfo = chatRepository.findFirstByUserMatchOrderBySendTimeDesc(userMatch);

                roomList.add(makeRoomResponse(userMatch, profile, profilePhoto, lastChatInfo));
            }
        }

        return roomList;
    }

    public RoomDto.RoomResponse makeRoomResponse(
            UserMatch userMatch, Profile profile, Optional<ProfilePhoto> profilePhoto, Optional<Chat> lastChatInfo) {

        RoomDto.RoomResponse response = RoomDto.RoomResponse.builder()
                .matchId(userMatch.getMatchId())
                .profilePhoto(null == profilePhoto ? null : profilePhoto.get().getPhotoUrl())
                .name(profile.getNickname())
                .unReadChatCount(chatRepository.findCountByIsChecked(false))
//                        .pinToTop()
                .build();

        if (lastChatInfo.isPresent()) {
            response = RoomDto.updateChatInfo(response, lastChatInfo.get());
        }

        return response;
    }

    public Long getMatchUserId(UserMatch userMatch, User loginUser) {

        if (userMatch.getUser1().getUserEmail().equals(loginUser.getUserEmail())) {
            return userMatch.getUser2().getUserId();
        }

        return userMatch.getUser1().getUserId();
    }

    public Set<RoomDto.SearchRoomResponse> searchMatchRoomList(RoomDto.SearchRoomRequest request, String userEmail) {

        User loginUser = userRepository.findOptionalByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        // 채팅방 내 검색 단어 포함 항목 조회 후 추가
        List<Chat> chatList = chatRepository.findAllByMessageContains(request.getSearchWord());

        Set<RoomDto.SearchRoomResponse> searchRoomList = new HashSet<>();

        if (0 < chatList.size()) {
            for (Chat chat : chatList) {
                UserMatch userMatch = chat.getUserMatch();

                Long matchUserId = getMatchUserId(userMatch, loginUser);

                // 상대방 프로필 정보 조회
                Profile profile = profileRepository.findById(
                                userRepository.findByUserId(matchUserId).getUserProfile().getId())
                        .orElseThrow(() -> new IllegalArgumentException("User Profile does not exist"));

                Optional<ProfilePhoto> profilePhoto = profilePhotoRepository.findByProfileId(profile.getId());

                RoomDto.SearchRoomResponse response = RoomDto.SearchRoomResponse.builder()
                        .matchId(userMatch.getMatchId())
                        .profilePhoto(null == profilePhoto ? null : profilePhoto.get().getPhotoUrl())
                        .name(profile.getNickname())
                        .build();

                searchRoomList.add(response);
            }
        }

        // 이름이 같은 사람 조회 후 추가
        List<UserMatch> userMatchList = matchRepository.findAllByUser1OrUser2(loginUser, loginUser);

        if (0 < userMatchList.size()) {
            for (UserMatch userMatch : userMatchList) {

                Long matchUserId = getMatchUserId(userMatch, loginUser);

                // 상대방 프로필 정보 조회
                Profile profile = profileRepository.findById(
                                userRepository.findByUserId(matchUserId).getUserProfile().getId())
                        .orElseThrow(() -> new IllegalArgumentException("User Profile does not exist"));

                Optional<ProfilePhoto> profilePhoto = profilePhotoRepository.findByProfileId(profile.getId());

                String matchUserNickname = profile.getNickname();

                if(matchUserNickname.equals(request.getSearchWord())) {
                    RoomDto.SearchRoomResponse response = RoomDto.SearchRoomResponse.builder()
                            .matchId(matchUserId)
                            .profilePhoto(null == profilePhoto ? null : profilePhoto.get().getPhotoUrl())
                            .name(matchUserNickname)
                            .build();

                    searchRoomList.add(response);
                }
            }
        }

        return searchRoomList;
    }
}
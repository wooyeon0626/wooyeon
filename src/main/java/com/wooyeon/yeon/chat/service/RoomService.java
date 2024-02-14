package com.wooyeon.yeon.chat.service;

import com.wooyeon.yeon.chat.domain.Chat;
import com.wooyeon.yeon.chat.dto.RoomDto;
import com.wooyeon.yeon.chat.repository.ChatRepository;
import com.wooyeon.yeon.common.security.SecurityService;
import com.wooyeon.yeon.exception.ExceptionCode;
import com.wooyeon.yeon.exception.WooyeonException;
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
    private final SecurityService securityService;

    public RoomDto.RoomResponse matchRoomList() throws WooyeonException {

        User loginUser = userRepository.findOptionalByEmail(securityService.getCurrentUserEmail())
                .orElseThrow(() -> new WooyeonException(ExceptionCode.LOGIN_USER_NOT_FOUND));

        List<UserMatch> userMatchList = matchRepository.findAllByUser1OrUser2(loginUser, loginUser);

        List<RoomDto.ChatResponse> result = new ArrayList<>();

        if (0 < userMatchList.size() && !userMatchList.isEmpty()) {
            for (UserMatch userMatch : userMatchList) {
                Long matchUserId = getMatchUserId(userMatch, loginUser);

                User matchUser = userRepository.findOptionalByUserId(matchUserId)
                        .orElseThrow(() -> new WooyeonException(ExceptionCode.USER_NOT_FOUND));

                Optional<Profile> profile = Optional.empty();
                Optional<ProfilePhoto> profilePhoto = Optional.empty();

                if (null != matchUser.getUserProfile()) {
                    profile = profileRepository.findById(matchUser.getUserProfile().getId());
                    profilePhoto = profilePhotoRepository.findByProfileId(profile.get().getId());
                }

                Optional<Chat> lastChatInfo = chatRepository.findFirstByUserMatchOrderBySendTimeDesc(userMatch);

                result.add(makeRoomResponse(userMatch, profile, profilePhoto, lastChatInfo));
            }
        }

        return RoomDto.RoomResponse.builder()
                .chatRoomList(result)
                .build();
    }

    public RoomDto.ChatResponse makeRoomResponse(
            UserMatch userMatch, Optional<Profile> profile, Optional<ProfilePhoto> profilePhoto, Optional<Chat> lastChatInfo) {

        RoomDto.ChatResponse response = RoomDto.ChatResponse.builder()
                .matchId(userMatch.getMatchId())
                .unReadChatCount(chatRepository.countByIsCheckedAndUserMatch(false, userMatch))
                .pinToTop(userMatch.isPinToTop())
                .build();

        if (lastChatInfo.isPresent()) {
            response = RoomDto.updateChatInfo(response, lastChatInfo.get());
        }

        if (profile.isPresent()) {
            response = RoomDto.updateProfile(response, profile.get());
        }

        if (profilePhoto.isPresent()) {
            response = RoomDto.updateProfilePhoto(response, profilePhoto.get());
        }

        return response;
    }

    public Long getMatchUserId(UserMatch userMatch, User loginUser) {

        if (userMatch.getUser1().getUserEmail().equals(loginUser.getUserEmail())) {
            return userMatch.getUser2().getUserId();
        }

        return userMatch.getUser1().getUserId();
    }

    public List<RoomDto.SearchRoomResponse> searchMatchRoomList(String searchWord) {

        User loginUser = userRepository.findOptionalByEmail(securityService.getCurrentUserEmail())
                .orElseThrow(() -> new WooyeonException(ExceptionCode.USER_NOT_FOUND));

        List<RoomDto.SearchRoomResponse> searchRoomList = new ArrayList<>();

        // 이름이 같은 사람 조회 후 추가
        List<UserMatch> userMatchList = matchRepository.findAllByUser1OrUser2(loginUser, loginUser);

        if (0 < userMatchList.size() && !userMatchList.isEmpty()) {
            for (UserMatch userMatch : userMatchList) {

                Long matchUserId = getMatchUserId(userMatch, loginUser);

                User matchUser = userRepository.findOptionalByUserId(matchUserId)
                        .orElseThrow(() -> new WooyeonException(ExceptionCode.USER_NOT_FOUND));

                // 상대방 프로필 정보 조회
                Optional<Profile> profile = Optional.empty();
                Optional<ProfilePhoto> profilePhoto = Optional.empty();
                String matchUserNickname = null;

                if (null != matchUser.getUserProfile()) {
                    profile = profileRepository.findById(matchUser.getUserProfile().getId());
                    profilePhoto = profilePhotoRepository.findByProfileId(profile.get().getId());
                    matchUserNickname = profile.get().getNickname();
                }

                if (null != searchWord && null != matchUserNickname && matchUserNickname.contains(searchWord)) {
                    RoomDto.SearchRoomResponse response = RoomDto.SearchRoomResponse.builder()
                            .matchId(matchUserId)
                            .name(matchUserNickname)
                            .build();

                    if (profile.isPresent()) {
                        response = RoomDto.updateProfile(response, profile.get());
                    }

                    if (profilePhoto.isPresent()) {
                        response = RoomDto.updateProfilePhoto(response, profilePhoto.get());
                    }

                    searchRoomList.add(response);
                }
            }
        }

        return searchRoomList;
    }
}
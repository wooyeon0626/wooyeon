package com.wooyeon.yeon.likeManage.controller;

import com.wooyeon.yeon.likeManage.dto.*;
import com.wooyeon.yeon.likeManage.repository.LikeRepository;
import com.wooyeon.yeon.likeManage.service.LikeService;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author heesoo
 */
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeRepository likeRepository;
    private final LikeService likeService;
    private final UserService userService;

    /**
     * 본인과 좋아요 한 userId를 입력받아서 like합니다.
     *
     * @param dto
     * @return 성공시 true
     * @deprecated
     */
    @Deprecated
    @PostMapping("/like/user/fromUserid")
    public boolean doLike(@RequestBody LikeDto dto) {
        User likeFromUser = userService.findByUserId(dto.getLikeFromUserId());
        User likeToUser = userService.findByUserId(dto.getLikeToUserId());
        likeService.saveUserLike(likeFromUser, likeToUser);
        return true;
    }

    /**
     * 내가 좋아하는 사람 리스트
     * 나의 userCode 받아서 내가 좋아하는 사람 리스트를 Page 형태로 반환
     *
     * @param myInfo
     * @param pageable
     * @return
     */
    @PostMapping("/like/to")
    public Page<ResponseProfileDto> findMyLike(@RequestBody MyUniqueInfoDto myInfo, Pageable pageable) {
        myInfo.setMyUserid(likeRepository.findUserIdByUserCode(myInfo.getUserCode()));
        return likeRepository.findProfilesILiked(myInfo, pageable);
    }

    /**
     * 본인과 좋아요 한 유저들의 userCode(UUID)를 입력받아서 like합니다.
     *
     * @param dto
     * @return response : responseMessage 포함된 객체
     */
    @PostMapping("/like/user")
    public CreateLikeResponse doLike(@RequestBody RequestLikeRequestDto dto) {
        User likeFromUser = userService.findByUserUUID(dto.getLikeFromUserUUID());
        User likeToUser = userService.findByUserUUID(dto.getLikeToUserUUID());
        CreateLikeResponse response = likeService.saveUserLike(likeFromUser, likeToUser);

        // 매치되었으면 기존의 like table에서 서로의 like 삭제.
        //boolean isMatch = likeService.checkMatch(likeFromUser.getUserId(), likeToUser.getUserId());
        if (response.getResponseMessage().equals("매치됨")) {
            likeService.deleteUserLikeRow(likeFromUser.getUserId(), likeToUser.getUserId());
            response.setUserLikeDeleteMessage("서로의 like 삭제");
        }
        return response;
    }

    /**
     * 나를 좋아하는 사람 리스트
     * 나의 userCode받아서 나를 좋아하사는 사람 리스트를 Page형태로 반환
     *
     * @param myInfo pageable
     * @return
     */
    @PostMapping("/like/from")
    public Page<ResponseProfileDto> findLikeMe(@RequestBody MyUniqueInfoDto myInfo, Pageable pageable) {
        myInfo.setMyUserid(likeRepository.findUserIdByUserCode(myInfo.getUserCode()));
        if (myInfo.getMyUserid() == null) {
            System.out.println("usercode : " + myInfo.getUserCode());
            System.out.println("에러임?");
            return null;
        }
        return likeRepository.findProfilesWhoLikedMe(myInfo, pageable);
    }

    @PostMapping("/like/matched")
    public Page<ResponseProfileDto> findMatchProfileList(@RequestBody MyUniqueInfoDto myInfo, Pageable pageable) {
        myInfo.setMyUserid(likeRepository.findUserIdByUserCode(myInfo.getUserCode()));
        return likeRepository.findProfilesMachedMe(myInfo, pageable);
    }
}

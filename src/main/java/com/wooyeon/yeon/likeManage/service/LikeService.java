package com.wooyeon.yeon.likeManage.service;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.dto.LikeDto;
import com.wooyeon.yeon.likeManage.repository.LikeRepository;
import com.wooyeon.yeon.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    //like 생성
    public UserLike createLike(User likeFrom, User likeTo) {
        UserLike userLike = UserLike.builder()
                .likeFrom(likeFrom)
                .likeTo(likeTo)
                .build();

        return likeRepository.save(userLike);
    }

    //매치되었는지 확인 하는 메서드
    public boolean checkMatch(User user1, User user2) {
        UserLike like1 = likeRepository.findByLikeFromAndLikeTo(user1, user2);
        UserLike like2 = likeRepository.findByLikeFromAndLikeTo(user2, user1);

        return like1 != null && like2 != null;
    }

    // 내가 한 좋아요 조회
    public List<LikeDto> findMyLikeList(Long likeId) {
        //userlike의 likefrom == 본인 이름인것을 반환.
        //List<UserLike> userLikeList = likeRepository.findBy
    }

    private LikeDto convertToLikeDto(UserLike userLike) {
        LikeDto dto = new LikeDto();
        dto.setLikeId(userLike.getLikeId());
        dto.setLikeToUserId(userLike.getLikeTo().getUserId());
        dto.setLikeFromUserId(userLike.getLikeFrom().getUserId());

        return dto;
    }

    // 내가 받은 좋아요 조회

}

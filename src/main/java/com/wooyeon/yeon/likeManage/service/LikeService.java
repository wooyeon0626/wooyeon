package com.wooyeon.yeon.likeManage.service;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.likeManage.repository.LikeRepository;
import com.wooyeon.yeon.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}

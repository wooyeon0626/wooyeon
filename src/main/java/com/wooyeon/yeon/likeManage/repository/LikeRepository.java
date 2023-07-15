package com.wooyeon.yeon.likeManage.repository;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<UserLike, Long> {
    UserLike findByLikeFromAndLikeTo(User likeFrom, User likeTo);//LikeFrom과 LikeTo를 찾는다.
}

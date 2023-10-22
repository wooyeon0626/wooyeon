package com.wooyeon.yeon.likeManage.repository;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * spring data jpa 활용
 *
 * @author heesoo
 */
public interface LikeRepository extends JpaRepository<UserLike, Long>, LikeRepositoryFindProfileList {
    @Query("select count(userLike) from UserLike userLike " +
            "where (userLike.likeFrom.userId = :fromId and userLike.likeTo.userId = :toId) ")
    long countMatch(@Param("fromId") Long fromId, @Param("toId") Long toId);

    UserLike findByLikeFromAndLikeTo(User likeFromUser, User likeToUser);

    // 유저 코드로 유저 아이디 가져옴.
    @Query("select user.userId from User user where user.userCode = :myUserCode")
    Long findUserIdByUserCode(@Param("myUserCode") UUID myUserCode);
}

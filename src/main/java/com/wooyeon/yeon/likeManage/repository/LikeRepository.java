package com.wooyeon.yeon.likeManage.repository;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * spring data jpa 활용
 *
 * @author heesoo
 */
public interface LikeRepository extends JpaRepository<UserLike, Long>, LikeRepositoryFindProfileList {
    @Query("select count(userLike) from UserLike userLike " +
            "where (userLike.likeFrom.userId = :fromId and userLike.likeTo.userId = :toId) ")
    int countMatch(@Param("fromId") Long fromId, @Param("toId") Long toId);

    /**
     * 해당하는 user_like를 찾아줌.
     * @param likeFromUser
     * @param likeToUser
     * @return UserLike
     */
    UserLike findByLikeFromAndLikeTo(User likeFromUser, User likeToUser);

    Optional<UserLike> findByLikeFromAndLikeTo(Long likeFromUser, Long likeToUser);

    // 유저 코드로 유저 아이디 가져옴.
    @Query("select user.userId from User user where user.userCode = :myUserCode")
    Long findUserIdByUserCode(@Param("myUserCode") UUID myUserCode);

    // 유저 id로 유저 가져옴.
    @Query("select user from User user where user.userId = :myUserId")
    User findUserByUserId(@Param("myUserId") Long myUserId);
}

package com.wooyeon.yeon.likeManage.repository;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<UserLike, Long> {
    //spring data jpa 활용
    @Query("select count(*) from UserLike userLike " +
            "where (userLike.likeFrom.userId = :fromId and userLike.likeTo.userId = :toId) ")
    long countMatch(@Param("fromId") Long fromId, @Param("toId") Long toId);
}

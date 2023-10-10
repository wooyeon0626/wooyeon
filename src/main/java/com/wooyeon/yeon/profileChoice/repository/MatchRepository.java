package com.wooyeon.yeon.profileChoice.repository;

import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 정의 Repository 상속받아 활용
 */
public interface MatchRepository extends JpaRepository<UserMatch, Long>, MatchRepositoryRecommandUserList {

    Optional<List<UserMatch>> findAllByUserLike1(User UserLikeId1);
    Optional<UserMatch> findAllByUserLike1OOrUserLike2(Long userId);
}

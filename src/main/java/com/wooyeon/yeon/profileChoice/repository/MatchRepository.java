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

    List<UserMatch> findAllByUser1OrUser2(User userId1, User userId2);
    Optional<List<UserMatch>> findAllByUser1(Long userId);
}

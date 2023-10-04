package com.wooyeon.yeon.profileChoice.repository;

import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 사용자 정의 Repository 상속받아 활용
 */
public interface MatchRepository extends JpaRepository<UserMatch, Long>, MatchRepositoryRecommandUserList {
}

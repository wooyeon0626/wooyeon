package com.wooyeon.yeon.profileChoice.repository;

import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchRepository extends JpaRepository<UserMatch, Long>, MatchRepositoryRecommandUserList {
}

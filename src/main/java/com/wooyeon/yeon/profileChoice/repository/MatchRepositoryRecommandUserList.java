package com.wooyeon.yeon.profileChoice.repository;

import com.wooyeon.yeon.profileChoice.dto.RecommandUserCondition;
import com.wooyeon.yeon.profileChoice.dto.RecommandUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 사용자 정의 Repository, 구현 클래스는 MatchRepositoryImpl
 */
public interface MatchRepositoryRecommandUserList {
    Page<RecommandUserDto> searchUserProfileSimple(RecommandUserCondition condition, Pageable pageable);
}

package com.wooyeon.yeon.profileChoice.repository;

import com.wooyeon.yeon.profileChoice.dto.RecommandUserCondition;
import com.wooyeon.yeon.profileChoice.dto.RecommandUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchRepositoryRecommandUserList {
    Page<RecommandUserDto> searchUserProfileSimple(RecommandUserCondition condition, Pageable pageable);
}

package com.wooyeon.yeon.profileChoice.service;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * @author heesoo
 */
@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;

    @Transactional
    public String createMatch(UserLike userLike1, UserLike userLike2) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        UserMatch userMatch = UserMatch.builder()
                .userLike1(userLike1)
                .userLike2(userLike2)
                .generateTime(currentTimestamp)
                .build();

        matchRepository.save(userMatch);
        return "매치테이블 생성";
    }
}

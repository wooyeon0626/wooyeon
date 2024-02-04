package com.wooyeon.yeon.profileChoice.service;

import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import com.wooyeon.yeon.user.domain.User;
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
    public String createMatch(User user1, User user2) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        UserMatch userMatch = UserMatch.builder()
                .user1(user1)
                .user2(user2)
                .generateTime(currentTimestamp)
                .build();

        matchRepository.save(userMatch);
        // String이 아니라 의미있는 vo로 return 변경 필요
        return "매치테이블 생성";
    }
}

package com.wooyeon.yeon.profileChoice.service;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.profileChoice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;

    //userMatch 생성 후 저장.
    public UserMatch createMatch(UserLike userLike1, UserLike userLike2) {
        UserMatch userMatch = UserMatch.builder()
                .userLike1(userLike1)
                .userLike2(userLike2)
                .generateTime(new Timestamp(System.currentTimeMillis()))
                .build();

        return matchRepository.save(userMatch);
    }
}

package com.wooyeon.yeon.profileChoice.domain;

import com.wooyeon.yeon.likeManage.domain.Like;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match {
    @Id
    @GeneratedValue
    private Long matchId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_id_1")
    private Like like1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_id_2")
    private Like like2;
    private boolean isMatch;

    private Timestamp generateTime;

    @Builder
    public Match(Long matchId, Like like1, Like like2, boolean isMatch, Timestamp generateTime) {
        this.matchId = matchId;
        this.like1 = like1;
        this.like2 = like2;
        this.isMatch = isMatch;
        this.generateTime = generateTime;
    }
}

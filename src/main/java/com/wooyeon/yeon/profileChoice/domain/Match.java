package com.wooyeon.yeon.profileChoice.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import com.wooyeon.yeon.likeManage.domain.Like;

@Entity
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
}

package com.wooyeon.yeon.profileChoice.domain;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import lombok.*;
import com.wooyeon.yeon.likeManage.domain.UserLike;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMatch {
    @Id
    @GeneratedValue
    private Long matchId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_like_id1")
    private UserLike userLike1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_like_id2")
    private UserLike userLike2;

    private Timestamp generateTime;

    @Builder
    public UserMatch(Long matchId, UserLike userLike1, UserLike userLike2, Timestamp generateTime) {
        this.matchId = matchId;
        this.userLike1 = userLike1;
        this.userLike2 = userLike2;
        this.generateTime = generateTime;
    }
}

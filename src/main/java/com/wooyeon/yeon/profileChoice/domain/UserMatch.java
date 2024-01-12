package com.wooyeon.yeon.profileChoice.domain;

import com.wooyeon.yeon.likeManage.domain.UserLike;
import com.wooyeon.yeon.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.*;
import com.wooyeon.yeon.likeManage.domain.UserLike;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 유저의 매치됨을 확인하는 엔티티
 *
 * @author heesoo
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMatch {
    @Id
    @GeneratedValue
    private Long matchId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id1")
    private User user1;
    //@JoinColumn(name = "user_like_id1")
    //private UserLike userLike1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id2")
    private User user2;
    //@JoinColumn(name = "user_like_id2")
    //private UserLike userLike2;

    private Timestamp generateTime;

    @Builder
    public UserMatch(Long matchId, User user1, User user2, Timestamp generateTime) {
        this.matchId = matchId;
//        this.userLike1 = userLike1;
//        this.userLike2 = userLike2;
        this.user1 = user1;
        this.user2 = user2;
        this.generateTime = generateTime;
    }
}

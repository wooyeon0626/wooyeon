package com.wooyeon.yeon.profileChoice.domain;

import com.wooyeon.yeon.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id1")
    private User user1;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id2")
    private User user2;

    private Timestamp generateTime;

    private boolean pinToTop;

    @Builder
    public UserMatch(Long matchId, User user1, User user2, Timestamp generateTime) {
        this.matchId = matchId;
        this.user1 = user1;
        this.user2 = user2;
        this.generateTime = generateTime;
    }
}

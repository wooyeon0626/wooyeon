package com.wooyeon.yeon.profileChoice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMatch {
    @Id
    @GeneratedValue
    private Long matchId;

    @OneToMany(mappedBy = "userMatch", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserLike> userLikes = new ArrayList<>();
    private boolean isMatch;

    private Timestamp generateTime;

    @Builder
    public UserMatch(Long matchId, List<UserLike> userLikes, boolean isMatch, Timestamp generateTime) {
        this.matchId = matchId;
        this.userLikes = userLikes;
        this.isMatch = isMatch;
        this.generateTime = generateTime;
    }
}

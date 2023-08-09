package com.wooyeon.yeon.likeManage.domain;

import com.wooyeon.yeon.profileChoice.domain.UserMatch;
import com.wooyeon.yeon.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLike {
    @Id
    @GeneratedValue
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_to", nullable = false)
    private User likeTo; // 누구를 좋아하니?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_from", nullable = false)
    private User likeFrom; // 누가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private UserMatch userMatch;

    @Builder
    public UserLike(Long likeId, User likeTo, User likeFrom, UserMatch userMatch) {
        this.likeId = likeId;
        this.likeTo = likeTo;
        this.likeFrom = likeFrom;
        this.userMatch = userMatch;
    }
}

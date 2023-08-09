package com.wooyeon.yeon.likeManage.domain;

import com.wooyeon.yeon.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserLike {
    @Id
    @GeneratedValue
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_to_user_id", nullable = false)
    private User likeTo; // 누구를 좋아하니?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_from_user_id", nullable = false)
    private User likeFrom; // 누가

    @Builder
    public UserLike(Long likeId, User likeTo, User likeFrom) {
        this.likeId = likeId;
        this.likeTo = likeTo;
        this.likeFrom = likeFrom;
    }
}

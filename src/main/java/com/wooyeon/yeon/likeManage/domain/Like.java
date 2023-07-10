package com.wooyeon.yeon.likeManage.domain;

import com.wooyeon.yeon.profileChoice.domain.Match;
import com.wooyeon.yeon.user.domain.User;

import javax.persistence.*;

@Entity
public class Like {
    @Id
    @GeneratedValue
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_to", nullable = false)
    private User likeTo; // 누구를 좋아하니?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_from", nullable = false)
    private User likeFrom; // 누가

    @OneToOne(mappedBy = "like", fetch = FetchType.LAZY)
    private Match match;

}

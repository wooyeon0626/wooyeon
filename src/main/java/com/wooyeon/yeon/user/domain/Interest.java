package com.wooyeon.yeon.user.domain;

import javax.persistence.*;

@Entity
public class Interest {
    @Id
    private Long interestId;
    private String interest;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;
}

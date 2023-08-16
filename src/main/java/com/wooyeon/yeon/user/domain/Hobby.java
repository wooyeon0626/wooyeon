package com.wooyeon.yeon.user.domain;

import javax.persistence.*;

@Entity
public class Hobby {
    @Id
    private Long hobbyId;

    private String hobby;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;
}
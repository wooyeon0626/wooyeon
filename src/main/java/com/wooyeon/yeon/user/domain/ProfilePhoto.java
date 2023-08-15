package com.wooyeon.yeon.user.domain;

import javax.persistence.*;

@Entity
public class ProfilePhoto {
    @Id
    private Long profilePhotoId;
    private String profilePhoto;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;
}
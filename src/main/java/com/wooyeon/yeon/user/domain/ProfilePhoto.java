package com.wooyeon.yeon.user.domain;

import javax.persistence.*;

@Entity
public class ProfilePhoto {
    @Id
    private Long profilePhotoId;
    private String profilePhoto;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
}
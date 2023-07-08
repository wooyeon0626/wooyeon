package com.wooyeon.yeon.user.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProfilePhoto {
    @Id
    private String profilePhotoId;
    private String profilePhoto;
}

package com.wooyeon.yeon.user.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class ProfilePhoto {
    @Id
    @GeneratedValue
    private Long profilePhotoId;
    private String photoUrl;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Builder
    public ProfilePhoto(String photoUrl, Profile profile) {
        this.photoUrl=photoUrl;
        this.profile=profile;
    }
}

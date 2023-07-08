package com.wooyeon.yeon.user.domain;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 50, nullable = false)
    private String phone;

    @Column(nullable = false)
    private char gender;

    @Column(length = 50, nullable = false)
    private String nickname;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PROFILE_PHOTO_ID")
    private List<ProfilePhoto> profilePhoto=new ArrayList<>();

    @Column(length = 50, nullable = false)
    private String birthday;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String locationInfo;

    @Column(length = 100, nullable = false)
    private String gpsLocationInfo;

    @Column(length = 20)
    private String mbti;

    @Column(length = 50)
    private String intro;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "HOBBY_ID")
    private List<Hobby> hobby=new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "INTEREST_ID")
    private List<Interest> interest=new ArrayList<>();

    @Builder
    public User(Long userId, String phone, char gender, String nickname, List<ProfilePhoto> profilePhoto, String birthday, String email, String locationInfo, String gpsLocationInfo, String mbti, String intro, List<Hobby> hobby, List<Interest> interest) {
        this.userId=userId;
        this.phone=phone;
        this.gender=gender;
        this.nickname=nickname;
        this.profilePhoto=profilePhoto;
        this.birthday=birthday;
        this.email=email;
        this.locationInfo=locationInfo;
        this.gpsLocationInfo=gpsLocationInfo;
        this.mbti=mbti;
        this.intro=intro;
        this.hobby=hobby;
        this.interest=interest;
    }

}
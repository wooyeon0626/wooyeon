package com.wooyeon.yeon.profileChoice.controller;

import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InitUser {
    private final InitMemberService initMemberService;


/*    @PostConstruct
    public void init() {
        initMemberService.init();
    }*/

    @Component
    static class InitMemberService {
        @PersistenceContext
        private EntityManager em;

        /*@Transactional
        public void init() {
            for (Long i = 1L; i <= 10L; i++) {
                UUID uuid = UUID.randomUUID();
                User user = User.builder()
                        .email("sss@naver.com")
                        .phone("01012341234")
                        .userCode(uuid)
                        .build();
                System.out.println(user.getUserCode());
                Profile profile = Profile.builder()
                        .profilePhotos(null)
                        .birthday(generateRandomBirthday())
                        .intro("자기소개샘플")
                        .mbti("MBTI")
                        .gpsLocationInfo("3km")
                        .nickname("닉네임" + i)
//                        .hobbies(null)
//                        .interests(null)
                        .locationInfo("주소" + i)
                        .gender('M')
                        .build();
                user.setProfile(profile);
                em.persist(user);
                em.persist(profile);
            }
        }*/
    }
}

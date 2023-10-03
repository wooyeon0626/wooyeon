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

/**
 * @author heesoo
 */
@Component
@RequiredArgsConstructor
public class InitUser {
    private final InitMemberService initMemberService;

    /**
     * 서비스 실행전에 수행되는 메서드입니다.
     */
    @PostConstruct
    public void init() {
        //initMemberService.init();
    }


    @Component
    static class InitMemberService {
        @PersistenceContext
        private EntityManager em;

        /**
         * 테스트용으로 유저를 생성합니다.
         */
        @Transactional
        public void init() {
            for (Long i = 1L; i <= 10L; i++) {
                User user = User.builder()
                        .email("sss@naver.com")
                        .phone("01012341234")
                        .userCode(UUID.randomUUID())
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
//                        .locationInfo("주소" + i)
                        .gender('M')
                        .build();
                user.setProfile(profile);
                em.persist(user);
                em.persist(profile);
            }
        }

        /**
         * 무작위 생일 문자열을 생성합니다.
         *
         * @return randomBirthday
         */
        public static String generateRandomBirthday() {
            Random random = new Random();

            // 랜덤한 연도 생성 (1900년부터 2023년 사이)
            int year = random.nextInt(124) + 1900; // 2023 - 1900 + 1

            // 랜덤한 월 생성 (1월부터 12월 사이)
            int month = random.nextInt(12) + 1;

            // 각 월의 일수를 배열로 정의
            int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            // 윤년인 경우 2월의 일수를 29일로 변경
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                daysInMonth[2] = 29;
            }

            // 랜덤한 일 생성
            int day = random.nextInt(daysInMonth[month]) + 1;

            // 날짜를 2자리 문자열로 포맷팅
            String formattedMonth = String.format("%02d", month);
            String formattedDay = String.format("%02d", day);

            // 랜덤한 생년월일 문자열 생성
            String randomBirthday = year + formattedMonth + formattedDay;

            return randomBirthday;
        }
    }
}
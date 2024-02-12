package com.wooyeon.yeon.user;

import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.UserRepository;
import com.wooyeon.yeon.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
//
//    @Test
//    public void createUser() {
//        User user = User.builder()
//                .email("match1@naver.com")
//                .phone("01012345678")
//                .password(passwordEncoder.encode("1234"))
//                .build();
//
//        userRepository.save(user);
//    }
//
    // passwordEncoder 사용
    @Test
    public void pwEncoderUser() {
        User user = User.builder()
                .email("ez123@gmail.com")
                .userCode(UUID.randomUUID())
                .password(passwordEncoder.encode("!aaaa0000"))
                .build();
        userRepository.save(user);
    }

    /*
    @Test
    public void createProfileTest() {
        Profile profile = Profile.builder()
                .gender('F')
                .nickname("hiz0")
                .birthday("19951010")
                .build();
    }
    */

    // sha256 + salt 사용
    /*@Test
    public void shaUser() {
        String pw = userService.encryptSha256("1234");
        String salt = userService.createSalt();
        String fin = userService.encryptSha256("1234"+salt);
        User usersh = User.builder()
                .email("s123@gmail.com")
                .userCode(UUID.randomUUID())
                .password("{bcrypt}$2a$10$"+salt+fin)
                .salt(salt)
                .build();
        userRepository.save(usersh);
    }*/
}

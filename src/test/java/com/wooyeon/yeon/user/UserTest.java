package com.wooyeon.yeon.user;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void createUser() {
        User user = User.builder()
                .email("young@naver.com")
                .phone("01012345678")
                .password(passwordEncoder.encode("1234"))
                .build();

        userRepository.save(user);
    }

}

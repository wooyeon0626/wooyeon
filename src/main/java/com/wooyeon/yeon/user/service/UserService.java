package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.EmailAuthRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final EmailAuthService emailAuthService;

    @Transactional
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Transactional
    public User findByUserUUID(UUID userCode) {
        return userRepository.findByUserCode(userCode);
    }

    public void newRsaEncrypt() {

    }
}

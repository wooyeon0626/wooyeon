package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.UserDto;
import com.wooyeon.yeon.user.repository.EmailAuthRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final EmailAuthService emailAuthService;

    @Autowired
    public UserService(UserRepository userRepository, EmailAuthRepository emailAuthRepository, EmailAuthService emailAuthService) {
        this.userRepository = userRepository;
        this.emailAuthRepository = emailAuthRepository;
        this.emailAuthService = emailAuthService;
    }

    @Transactional
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }
}

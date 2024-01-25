package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.RsaPublicResponseDto;
import com.wooyeon.yeon.user.repository.EmailAuthRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import com.wooyeon.yeon.user.service.encrypt.RsaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RsaUtil rsaUtil;

    @Transactional
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Transactional
    public User findByUserUUID(UUID userCode) {
        return userRepository.findByUserCode(userCode);
    }

    public RsaPublicResponseDto sendRsaPublicKey() {
        RsaPublicResponseDto rsaPublicResponseDto = RsaPublicResponseDto.builder()
                .publicKey(rsaUtil.sendPublicKey())
                .build();

        log.info("Service public key: {}", rsaUtil.sendPublicKey());

        return rsaPublicResponseDto;
    }
}

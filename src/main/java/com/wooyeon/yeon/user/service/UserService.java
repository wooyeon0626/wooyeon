package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.MemberRegisterRequestDto;
import com.wooyeon.yeon.user.dto.MemberRegisterResponseDto;
import com.wooyeon.yeon.user.dto.UserDto;
import com.wooyeon.yeon.user.repository.EmailAuthRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public MemberRegisterResponseDto registerMember(MemberRegisterRequestDto requestDto) {
        // 이메일 중복 확인 로직 추가
        validateDuplicated(requestDto.getEmail());

        // 회원 정보 저장
        User user = User.builder()
                .email(requestDto.getEmail())
                .build();
        userRepository.save(user);

        // 이메일 인증 링크 발송
        emailAuthService.sendEmailVerification(user);

        // 회원 가입 응답 생성
        return MemberRegisterResponseDto.builder()
                .email(user.getEmail())
                .authToken(null) // 여기에는 authToken이 아직 없으므로 null로 설정
                .build();
    }

    // 이메일 중복 확인 로직 구현
    private void validateDuplicated(String email) {
        // 중복된 이메일이 이미 회원 테이블에 존재한다면 예외 처리
        if (emailAuthRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }
    }

    @Transactional
    public UserDto findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }
}

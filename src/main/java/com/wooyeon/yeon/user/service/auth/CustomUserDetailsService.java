package com.wooyeon.yeon.user.service.auth;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail + "을/를 데이터베이스에서 찾을 수 없습니다."));

        // DB 에 Studio 값이 존재한다면 studioDetails 객체로 만들어서 리턴
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");

        CustomStudioDetails studioDetails = CustomStudioDetails.builder()
                .id(studio.getId())
                .email(studio.getEmail())
                .password(studio.getPassword())
                .role((Collection<GrantedAuthority>) grantedAuthority)
                .build();

        return studioDetails;
    }

    @Override
    public User getLoginUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
}

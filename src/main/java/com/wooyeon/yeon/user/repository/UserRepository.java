package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    // 필요에 따라 다른 메서드들을 추가할 수 있습니다.
}
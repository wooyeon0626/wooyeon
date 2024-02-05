package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserId(Long userId);
    User findByUserCode(UUID userCode);
    User findByEmail(String email);
    Optional<User> findOptionalByEmail(String email);
    Optional<User> findOptionalByUserId(Long userId);
}

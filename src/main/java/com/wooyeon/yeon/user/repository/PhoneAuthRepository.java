package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.PhoneAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {
    Optional<PhoneAuth> findByVerifyCode(String verifyCode);

}

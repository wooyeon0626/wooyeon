package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.PhoneAuth;
import com.wooyeon.yeon.user.domain.QPhoneAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {
    // 휴대폰 중복 체크
    boolean existsByPhone(String phone);

    PhoneAuth findByPhoneAndVerifyCode(String phone, String verifyCode);

    @Modifying
    @Transactional
    @Query("DELETE FROM PhoneAuth e WHERE e.expireDate < :currentDateTime")
    void deleteExpiredRecords(@Param("currentDateTime") LocalDateTime currentDateTime);

}

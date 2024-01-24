package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    boolean existsByEmail(String email);

    EmailAuth findEmailAuthByEmail(String email);

    EmailAuth findEmailAuthByEmailAndAuthToken(String email, String authToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM EmailAuth e WHERE e.expireDate < :currentDateTime AND e.certification = false")
    void deleteExpiredRecords(@Param("currentDateTime") LocalDateTime currentDateTime);


}


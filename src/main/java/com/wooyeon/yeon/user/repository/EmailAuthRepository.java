package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.EmailAuth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    Optional<EmailAuth> findByAuthToken(String authToken);

    @Modifying
    @Query("DELETE FROM EmailAuth e WHERE e.email = :email")
    void deleteByEmail(@Param("email") String email);
}


package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserId(Long userId);
    User findByUserCode(UUID userCode);
    User findByPhone(String phone);

}
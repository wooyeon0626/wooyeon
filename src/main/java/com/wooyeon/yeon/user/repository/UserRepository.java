package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    UserDto findByUserId(Long userId);

}
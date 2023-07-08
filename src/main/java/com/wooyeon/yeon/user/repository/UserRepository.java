package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
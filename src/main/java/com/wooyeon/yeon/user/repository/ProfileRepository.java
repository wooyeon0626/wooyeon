package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByNicknameContains(String searchWord);

    Optional<Profile> findByUser(User user);

}

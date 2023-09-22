package com.wooyeon.yeon.user.repository;

import com.wooyeon.yeon.user.domain.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Long> {
}

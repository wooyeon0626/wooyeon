package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.ProfilePhoto;
import com.wooyeon.yeon.user.dto.ProfileRequestDto;
import com.wooyeon.yeon.user.dto.ProfileResponseDto;
import com.wooyeon.yeon.user.repository.ProfilePhotoRepository;
import com.wooyeon.yeon.user.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfilePhotoRepository profilePhotoRepository;

    public ProfileService(ProfileRepository profileRepository, ProfilePhotoRepository profilePhotoRepository) {
        this.profileRepository=profileRepository;
        this.profilePhotoRepository=profilePhotoRepository;
    }

    public ProfileResponseDto insertProfile(ProfileRequestDto profileRequestDto, List<MultipartFile> profilePhotoUpload) {

        Profile profile=Profile.builder()
                .gender(profileRequestDto.getGender())
                .nickname(profileRequestDto.getNickname())
                .birthday(profileRequestDto.getBirthday())
                .mbti(profileRequestDto.getMbti())
                .hobby(profileRequestDto.getHobby())
                .interest(profileRequestDto.getInterest())
                .intro(profileRequestDto.getIntro())
                .faceVerify(false)
                .build();
        profileRepository.save(profile);

        for(int i=0; i<profilePhotoUpload.size(); i++) {
            ProfilePhoto profilePhoto=ProfilePhoto.builder()
                    .photoUrl(UUID.randomUUID() + profilePhotoUpload.get(i).getOriginalFilename())
                    .profile(profile)
                    .build();
            profilePhotoRepository.save(profilePhoto);
        }

        ProfileResponseDto profileResponseDto=ProfileResponseDto.builder()
                .statusName("success")
                .build();
        return profileResponseDto;
    }
}

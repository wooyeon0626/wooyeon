package com.wooyeon.yeon.user.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.ProfilePhoto;
import com.wooyeon.yeon.user.dto.ProfileRequestDto;
import com.wooyeon.yeon.user.dto.ProfileResponseDto;
import com.wooyeon.yeon.user.repository.ProfilePhotoRepository;
import com.wooyeon.yeon.user.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfilePhotoRepository profilePhotoRepository;

    @Value("${spring.cloud.gcp.storage.bucket}") // application.yml에 써둔 bucket 이름
    private String bucketName;

    private Storage storage;

    public ProfileService(ProfileRepository profileRepository, ProfilePhotoRepository profilePhotoRepository, Storage storage) {
        this.profileRepository=profileRepository;
        this.profilePhotoRepository=profilePhotoRepository;
        this.storage=storage;
    }

    public ProfileResponseDto insertProfile(ProfileRequestDto profileRequestDto, List<MultipartFile> profilePhotoUpload) throws IOException {

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

        String ext="";
        String uuid="";

        for(int i=0; i<profilePhotoUpload.size(); i++) {
            ext=profilePhotoUpload.get(i).getContentType();
            uuid=UUID.randomUUID().toString();

            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(bucketName,uuid)
                            .setContentType(ext)
                            .build(),
                    profilePhotoUpload.get(i).getInputStream()
            );

            ProfilePhoto profilePhoto=ProfilePhoto.builder()
                    .photoUrl("https://storage.googleapis.com/our-audio-394406.appspot.com/"+uuid)
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

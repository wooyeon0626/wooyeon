package com.wooyeon.yeon.user.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.ProfilePhoto;
import com.wooyeon.yeon.user.dto.ProfileRequestDto;
import com.wooyeon.yeon.user.dto.ProfileResponseDto;
import com.wooyeon.yeon.user.repository.ProfilePhotoRepository;
import com.wooyeon.yeon.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfilePhotoRepository profilePhotoRepository;

    @Value("${spring.cloud.gcp.storage.bucket}") // application.yml에 써둔 bucket 이름
    private String bucketName;

    private final Storage storage;

    public ProfileResponseDto insertProfile(ProfileRequestDto profileRequestDto, List<MultipartFile> profilePhotoUpload) throws IOException {

        // Profile 테이블에 정보 저장
        Profile profile = Profile.builder()
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

        // GCS에 이미지 업로드
        StringBuilder ext = new StringBuilder(); // 파일의 contentType
        StringBuilder uuid = new StringBuilder(); // 저장할 때 쓸 파일 이름(uuid)
        for (MultipartFile multipartFile : profilePhotoUpload) {
            ext.append(multipartFile.getContentType());
            uuid.append(UUID.randomUUID());

            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(bucketName, uuid.toString())
                            .setContentType(ext.toString())
                            .build(),
                    multipartFile.getInputStream()
            );

            // profilePhoto 테이블에 해당 사진 url 저장
            ProfilePhoto profilePhoto = ProfilePhoto.builder()
                    .photoUrl("https://storage.googleapis.com/" + bucketName + "/" + uuid)
                    .profile(profile)
                    .build();
            profilePhotoRepository.save(profilePhoto);
        }

        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .statusCode(202)
                .statusName("success")
                .build();
        return profileResponseDto;
    }
}

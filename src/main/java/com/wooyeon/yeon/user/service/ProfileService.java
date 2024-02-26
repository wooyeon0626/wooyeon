package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.common.security.SecurityService;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.domain.ProfilePhoto;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.ProfileRequestDto;
import com.wooyeon.yeon.user.dto.ProfileResponseDto;
import com.wooyeon.yeon.user.repository.ProfilePhotoRepository;
import com.wooyeon.yeon.user.repository.ProfileRepository;
import com.wooyeon.yeon.user.repository.UserRepository;
import com.wooyeon.yeon.user.service.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ProfileRepository profileRepository;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    @Value("${lightsail.instanceName}") // Lightsail 인스턴스 이름
    private String lightsailInstanceName;

    @Value("${lightsail.accessKey}")
    private String accessKey;

    @Value("${lightsail.secretKey}")
    private String secretKey;

    @Value("${lightsail.region}")
    private String region;

    @Value("${lightsail.bucketName}")
    private String bucketName;

    private final JwtTokenProvider jwtTokenProvider;

    private S3Client createS3Client() {
        S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
        return s3Client;
    }

    public ProfileResponseDto insertProfile(ProfileRequestDto profileRequestDto, List<MultipartFile> profilePhotoUpload) throws IOException {
        // token에서 user 정보 추출하기
        String userEmail = securityService.getCurrentUserEmail();
        log.info("추출한 user Email : {}", userEmail);

        Optional<User> findUser = userRepository.findOptionalByEmail(userEmail);
        User user = userRepository.findByEmail(userEmail);
        log.debug("findUser 정보 : {}", findUser);
        log.debug("user 정보 : {}", user);

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
                .user(findUser.get())
                .build();
        profileRepository.save(profile);

        S3Client s3Client = createS3Client();
        lightsailFileUpload(profile, s3Client, profilePhotoUpload);

        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .statusCode(202)
                .statusName("success")
                .build();
        return profileResponseDto;
    }

    void lightsailFileUpload(Profile profile, S3Client s3Client, List<MultipartFile> profilePhotoUpload) throws IOException {
        // bucket에 파일 업로드
        StringBuilder ext = new StringBuilder(); // 파일의 contentType
        StringBuilder uuid = new StringBuilder(); // 저장할 때 쓸 파일 이름(uuid)
        for (MultipartFile multipartFile : profilePhotoUpload) {
            ext.append(multipartFile.getOriginalFilename().split("\\.")[1]);
            uuid.append(UUID.randomUUID());

            // 업로드할 파일 정보 설정
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uuid.toString()+"."+ext) // 확장자 추가
                    .contentType(ext.toString())
                    .build();

            // 파일 업로드
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));

            // profilePhoto 테이블에 해당 사진 url 저장
            ProfilePhoto profilePhoto = ProfilePhoto.builder()
                    .photoUrl("https://" + bucketName + ".s3." + region + ".amazonaws.com/" + uuid.toString() + "." + ext)
                    .profile(profile)
                    .build();
            profilePhotoRepository.save(profilePhoto);
        }
    }

    public HttpStatus updateUsersGpsLocation(User user, String gpsLocation) {
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(accessToken);
        //User user = userRepository.findByEmail(userEmail);
        Optional<Profile> profile = profileRepository.findByUser(user);
        log.debug("user 정보(gps): {}", profile);
        log.info("gpsLocation: {}", gpsLocation);

        profile.ifPresent(value -> value.updateGpsLocationInfo(gpsLocation));

        return HttpStatus.OK;
    }
}

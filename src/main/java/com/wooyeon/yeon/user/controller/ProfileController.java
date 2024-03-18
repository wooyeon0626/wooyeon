package com.wooyeon.yeon.user.controller;

import com.wooyeon.yeon.common.security.SecurityService;
import com.wooyeon.yeon.user.domain.Profile;
import com.wooyeon.yeon.user.dto.FindProfileResponseDto;
import com.wooyeon.yeon.user.dto.ProfileRequestDto;
import com.wooyeon.yeon.user.dto.InsertProfileResponseDto;
import com.wooyeon.yeon.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ProfileController {

    private final ProfileService profileService;
    private final SecurityService securityService;

    /** 프로필 등록 API */
    @PostMapping(value = "/users/register/profile")
    public ResponseEntity<InsertProfileResponseDto> createProfile(@RequestPart(value = "profileInfo") ProfileRequestDto profileRequestDto,
                                                                  @RequestPart(value = "profilePhoto", required = false) List<MultipartFile> profilePhotoUpload) throws IOException {
        InsertProfileResponseDto insertProfileResponseDto = profileService.insertProfile(profileRequestDto, profilePhotoUpload);
        return ResponseEntity.ok(insertProfileResponseDto);
    }

    /** 프로필 조회 API */
    @GetMapping("/users/profile")
    public ResponseEntity<FindProfileResponseDto> findProfile() {
        String loginEmail = securityService.getCurrentUserEmail();
        return ResponseEntity.ok(profileService.findProfile(loginEmail));
    }

    /** GPS 수신 API */
    @PostMapping(value = "/users/profile/gps", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HttpStatus> receiveUsersGps(@RequestBody String gpsLocation) {
//        String accessToken = parseBearerToken(request);
        String loginEmail = securityService.getCurrentUserEmail();
        log.info("loginEmail : {}", loginEmail);
        return ResponseEntity.ok(profileService.updateUsersGpsLocation(loginEmail, gpsLocation));
    }
}

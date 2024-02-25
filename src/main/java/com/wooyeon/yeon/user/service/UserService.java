package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.domain.UserRoles;
import com.wooyeon.yeon.user.dto.LoginDto;
import com.wooyeon.yeon.user.dto.PasswordEncryptRequestDto;
import com.wooyeon.yeon.user.dto.PasswordEncryptResponseDto;
import com.wooyeon.yeon.user.dto.RsaPublicResponseDto;
import com.wooyeon.yeon.user.repository.UserRepository;
import com.wooyeon.yeon.user.repository.UserRolesRepository;
import com.wooyeon.yeon.user.service.encrypt.AesUtil;
import com.wooyeon.yeon.user.service.encrypt.RsaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RsaUtil rsaUtil;
    private final AesUtil aesUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRolesRepository userRolesRepository;

    @Transactional
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Transactional
    public User findByUserUUID(UUID userCode) {
        return userRepository.findByUserCode(userCode);
    }

    public RsaPublicResponseDto sendRsaPublicKey() {
        RsaPublicResponseDto rsaPublicResponseDto = RsaPublicResponseDto.builder()
                .publicKey(RsaUtil.sendPublicKey())
                .build();

        return rsaPublicResponseDto;
    }

    public String decodeEncrypt(PasswordEncryptRequestDto passwordEncryptRequestDto)
            throws Exception {

        String encryptedKey = passwordEncryptRequestDto.getEncryptedKey();
        log.info("RSA 공개키로 암호화 된 키(encryptedKey) : {}", encryptedKey);

        // 1. |로 IV와 AES Key로 나누기
        String[] key = encryptedKey.split("\\|");
        String base64AesKey = key[1];
        String base64Iv = key[0];
        log.info("base64AesKey : {}", base64AesKey);
        log.info("base64Iv : {}", base64Iv);

        // 2. Base64 디코딩
        byte[] ivBytes = Base64.getDecoder().decode(base64Iv);

        // 3.RSA 개인키로 Session Key(AES Key) 복호화
        log.info("RSA 디코딩 시작");
        byte[] decodedKey = rsaUtil.rsaDecode(base64AesKey, RsaUtil.sendPrivateKey());

        log.info("디코딩된 IV: {}", ivBytes);
        log.info("복호화된 AES Key: {}", decodedKey);

        // 4. IV, SessionKey로 암호화된 비밀번호 복호화
        // AES Key 로 비밀번호 복호화해서 원문 받아오기
        String decodedPassword = aesUtil.decrypt(passwordEncryptRequestDto.getEncryptedPassword(), decodedKey, ivBytes);
        log.debug("AES로 복호화한 원문 : {}", decodedPassword);

        /*
        비밀번호 + salt를 SHA256으로 암호화
        String salt = createSalt();
        String password = decodedPassword+salt;
          log.info("salt : {}", salt);
          log.info("finalPassword : {}", finalPassword);
        */

        return decodedPassword;
    }

    public PasswordEncryptResponseDto savePassword(PasswordEncryptRequestDto passwordEncryptRequestDto) throws Exception {
        // 이미 등록된 이메일인지 확인
        boolean validateEmail = validateEmail(passwordEncryptRequestDto.getEmail());
        if (validateEmail) {
            // 이미 등록된 이메일인 경우 수행을 멈추고 중복됐다는 값을 return
            PasswordEncryptResponseDto duplicateResponseDto = PasswordEncryptResponseDto.builder()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .statusName("duplicated")
                    .build();
            return duplicateResponseDto;
        }

        // 암호화된 key, password Decode
        String decodedPassword = decodeEncrypt(passwordEncryptRequestDto);

        // passwordEncoder로 비밀번호 암호화 (2024.02.06 로그인과 암호화 방식 맞춤 수정)
        String finalPassword = passwordEncoder.encode(decodedPassword);
        log.debug("finalPassword : {}", finalPassword);

        // User 테이블에 저장
        User user = User.builder()
                .email(passwordEncryptRequestDto.getEmail())
                .emailAuth(true)
                .userCode(UUID.randomUUID())
                .password(finalPassword)
                .build();
        userRepository.save(user);

        // User Roles 추가
        UserRoles userRoles = UserRoles.builder()
                .userUserId(user.getUserId())
                .roles("USER_ROLES")
                .build();
        userRolesRepository.save(userRoles);

        // ResponseDto 구성
        PasswordEncryptResponseDto passwordEncryptResponseDto = PasswordEncryptResponseDto.builder()
                .statusCode(HttpStatus.SC_OK)
                .statusName("success")
                .build();
        return passwordEncryptResponseDto;
    }

    // salt 생성
    public String createSalt() {

        //1. Random, byte 객체 생성
        SecureRandom r = new SecureRandom ();
        byte[] salt = new byte[20];

        //2. 난수 생성
        r.nextBytes(salt);

        //3. byte To String (10진수의 문자열로 변경)
        StringBuffer sb = new StringBuffer();
        for(byte b : salt) {
            sb.append(String.format("%02x", b));
        };

        return sb.toString();
    }

    // SHA256 암호화
    public String encryptSha256(String password) {

        String shaEncryptedPassword = "";
        try {
            //1. SHA256 알고리즘 객체 생성
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //2. 비밀번호와 salt 합친 문자열에 SHA 256 적용
            md.update(password.getBytes());
            byte[] pwdSalt = md.digest();

            //3. byte To String (10진수의 문자열로 변경)
            StringBuffer sb = new StringBuffer();
            for (byte b : pwdSalt) {
                sb.append(String.format("%02x", b));
            }

            shaEncryptedPassword=sb.toString();
            log.info("SHA 암호화 후 : {}", shaEncryptedPassword);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return shaEncryptedPassword;
    }

    boolean validateEmail(String email) {
        if (userRepository.findByEmail(email) != null) {
            // 이미 등록된 이메일인 경우 true 값을 return
            return true;
        } else return false;
    }

    /*public void encryptLogin(PasswordEncryptRequestDto passwordEncryptRequestDto) throws Exception {
        // 암호화된 key, password Decode
        String password = decodeEncrypt(passwordEncryptRequestDto);
        LoginRequestDto loginRequestDto = null;
        loginRequestDto.updateEmail(passwordEncryptRequestDto.getEmail());
        loginRequestDto.updatePassword(password);
        loginService.login(loginRequestDto);
    }*/

}

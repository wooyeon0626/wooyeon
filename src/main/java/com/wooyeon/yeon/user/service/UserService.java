package com.wooyeon.yeon.user.service;

import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.dto.PasswordEncryptRequestDto;
import com.wooyeon.yeon.user.dto.PasswordEncryptResponseDto;
import com.wooyeon.yeon.user.dto.RsaPublicResponseDto;
import com.wooyeon.yeon.user.repository.UserRepository;
import com.wooyeon.yeon.user.service.encrypt.RsaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RsaUtil rsaUtil;

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

        log.info("Service public key: {}", RsaUtil.sendPublicKey());

        return rsaPublicResponseDto;
    }

    public PasswordEncryptResponseDto decodeEncrypt(PasswordEncryptRequestDto passwordEncryptRequestDto)
            throws NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        String encryptedKey = "ABCDEFGHI1234567";
        log.info("RSA 공개키로 암호화 된 키(decodedKey) : {}", encryptedKey);

        // RSA 개인키로 복호화해서 AES Key+IV 원문 받아오기
        String decodedKey = RsaUtil.rsaDecode(encryptedKey, RsaUtil.sendPrivateKey());
        log.info("RSA 공개키로 복호화한 키(decodedKey) : {}", decodedKey);

        // IV와 AES Key로 나누기

        log.info("IV: {}");
        log.info("AES Key: {}");

        // AES Key 로 비밀번호 복호화해서 원문 받아오기
        String decodedPassword = "ABC";
        log.info("AES로 복호화한 원문 : {}", decodedPassword);

        // 비밀번호 + salt를 SHA256으로 암호화
        String salt = createSalt();
        String password = decodedPassword+salt;
        String finalPassword = encryptSha256(password);

        // User 테이블에 저장
        User user = User.builder()
                .email(passwordEncryptRequestDto.getEmail())
                .emailAuth(true)
                .userCode(UUID.randomUUID())
                .password(finalPassword)
                .salt(salt)
                .build();
        userRepository.save(user);

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

}

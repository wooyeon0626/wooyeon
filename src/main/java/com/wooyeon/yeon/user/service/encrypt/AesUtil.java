package com.wooyeon.yeon.user.service.encrypt;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Log4j2
public class AesUtil {

    public static String decrypt(String ciphertext, byte[] aesKeyBytes, byte[] ivBytes) throws Exception {
        // AES 키 및 IV 디코딩
        // byte[] aesKeyBytes = Base64.getDecoder().decode(aesKeyBase64);
        // byte[] ivBytes = Base64.getDecoder().decode(ivBase64);

        // AES 키 및 IV 생성
        SecretKey secretKey = new SecretKeySpec(aesKeyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // Base64 디코딩
        byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);

        // AES 복호화 설정
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        // 복호화 수행
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
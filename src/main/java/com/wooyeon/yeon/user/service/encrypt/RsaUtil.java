package com.wooyeon.yeon.user.service.encrypt;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@Log4j2
public class RsaUtil {
    private static final String INSTANCE_TYPE = "RSA";
    private static final KeyPair keyPair = generateKeyPair();

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(INSTANCE_TYPE);
            keyPairGenerator.initialize(2048, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating RSA key pair", e);
        }
    }

    public static String rsaEncode(String password, String publicKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(INSTANCE_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, convertPublicKey(publicKey));

        byte[] passwordByte = cipher.doFinal(password.getBytes());
        return base64EncodeToString(passwordByte);
    }

    public static byte[] rsaDecode(String encryptedPassword, String privateKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedPasswordByte = Base64.getDecoder().decode(encryptedPassword.getBytes());
        log.info("RSA Util encryptedPasswordByte : {}", encryptedPasswordByte);
        log.info("encryptedPasswordByte 길이 : {}", encryptedPasswordByte.length);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, convertPrivateKey(privateKey));
        log.info("decrypt init 성공!!!");
        log.info("cipher 길이 : {}", cipher.doFinal(encryptedPasswordByte));

        return cipher.doFinal(encryptedPasswordByte);
    }

    public static PublicKey convertPublicKey(String publicKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance(INSTANCE_TYPE);
        byte[] publicKeyByte = Base64.getDecoder().decode(publicKey.getBytes());

        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByte));
    }

    public static PrivateKey convertPrivateKey(String privateKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance(INSTANCE_TYPE);
        byte[] privateKeyByte = Base64.getDecoder().decode(privateKey.getBytes());

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
    }

    public static String base64EncodeToString(byte[] byteData) {
        return Base64.getEncoder().encodeToString(byteData);
    }

    public static String sendPublicKey() {
        return base64EncodeToString(keyPair.getPublic().getEncoded());
    }
    public static String sendPrivateKey() {
        return base64EncodeToString(keyPair.getPrivate().getEncoded());
    }
}

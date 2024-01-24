package com.wooyeon.yeon.user.service.encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtil {
    private static final String INSTANCE_TYPE = "RSA";

    // 2048 bit RSA KeyPair 생성
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(INSTANCE_TYPE);
        keyPairGenerator.initialize(2048, new SecureRandom());

        return keyPairGenerator.genKeyPair();
    }

    public static String rsaEncode(String password, String publicKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(INSTANCE_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, convertPublicKey(publicKey));

        byte[] passwordByte = cipher.doFinal(password.getBytes());
        return base64EncodeToString(passwordByte);
    }

    public static String rsaDecode(String encryptedPassword, String privateKey)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedPasswordByte = Base64.getDecoder().decode(encryptedPassword.getBytes());

        Cipher cipher = Cipher.getInstance(INSTANCE_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, convertPrivateKey(privateKey));

        return new String(cipher.doFinal(encryptedPasswordByte));
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
}

package de.ostfale.beezle;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Encrypts password for local storing
 * Created by usauerbrei on 06.12.2017
 */
public class ProtectPassword {

    private static final String SALT = "fincon17";
    private static final String PW = "bepw1722";
    private static final int ITERATION_COUNT = 40000;
    private static final int KEY_LENGTH = 128;

    public static String encryptPassword(String password) throws Exception {
        SecretKeySpec secretKeySpec = createSecretKey(PW.toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        return encrypt(password, secretKeySpec);
    }


    public static String decryptPassword(String encryptedPassword) throws Exception {
        SecretKeySpec secretKeySpec = createSecretKey(PW.toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        return decrypt(encryptedPassword, secretKeySpec);
    }

    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    private static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = cipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = cipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String decrypt(String propertyLine, SecretKeySpec keySpec) throws GeneralSecurityException, IOException {
        String propertyName = propertyLine.split(":")[0];
        String propertyValue = propertyLine.split(":")[1];
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec((base64Decode(propertyName))));
        return new String(cipher.doFinal(base64Decode(propertyValue)), "UTF-8");
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] base64Decode(String property) {
        return Base64.getDecoder().decode(property);
    }
}

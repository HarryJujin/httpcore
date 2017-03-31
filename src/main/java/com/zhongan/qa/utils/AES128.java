package com.zhongan.qa.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AES128 {
    private static Logger logger    = LoggerFactory.getLogger(AES128.class);
    private static String algorithm = "AES";
    private static String encoding  = "UTF-8";
    private String        encryptionKey;

    private AES128(String key) {
        this.encryptionKey = key;
    }

    public static AES128 getInstance(String key) {
        return new AES128(key);
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(encoding));

            return Base64.encode(encryptedBytes);
        } catch (Exception e) {
            logger.error("encrypt failed.", e);
            return null;
        }
    }

    public String decrypt(String encrypted) {
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            byte[] plainBytes = cipher.doFinal(Base64.decode(encrypted));

            return new String(plainBytes, encoding);
        } catch (Exception e) {
            logger.error("decrypt failed.", e);
            return null;
        }
    }

    private Cipher getCipher(int cipherMode) throws UnsupportedEncodingException, NoSuchPaddingException,
            InvalidKeyException, NoSuchAlgorithmException {

        SecretKeySpec keySpecification = new SecretKeySpec(encryptionKey.getBytes(encoding), algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(cipherMode, keySpecification);
        return cipher;
    }
}

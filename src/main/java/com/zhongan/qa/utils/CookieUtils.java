package com.zhongan.qa.utils;

public class CookieUtils {

    private String        secretKey;
    private static String defaultSecretKey = "dEfAu1tS3cretKeY";

    private CookieUtils(String Key) {
        this.secretKey = Key;
    }

    public static CookieUtils getInstance() {
        return new CookieUtils(defaultSecretKey);
    }

    public static CookieUtils getInstance(String Key) {
        return new CookieUtils(Key);
    }

    public String encryptCookie(String content) {
        return AES128.getInstance(secretKey).encrypt(content);
    }

    public String decryptCookie(String cookie) {
        return AES128.getInstance(secretKey).decrypt(cookie);
    }
}

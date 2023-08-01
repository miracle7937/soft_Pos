package com.enkpay.ep_softpos_plugin.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {


    private static final String SECRET_KEY = "J/PYjc1ftDFK5+77U1PB80v2TamokGap5yCIP2YI6tQ=";
    private static final String INIT_VECTOR = "gaOr3uvhZEwFeSbRHwlHcg==";



    public static String encrypt(String strToEncrypt) {
        try {
            byte[]  key = Base64.getDecoder().decode(SECRET_KEY);
            SecretKeySpec   secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(Base64.getDecoder().decode(INIT_VECTOR));
            Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String encryptedText) {
        try {
            byte[] key = Base64.getDecoder().decode(SECRET_KEY);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(Base64.getDecoder().decode(INIT_VECTOR));
            Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }




}








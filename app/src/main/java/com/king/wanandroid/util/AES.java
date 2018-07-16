package com.king.wanandroid.util;


import android.text.TextUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public enum AES {

    INSTANCE;

    private static final String IV_STRING = "ABCD1234EFGH5678";
    public static final String DEFAULT_KEY = "QWERTYUIOPASDFGH";
    private static final String charset = "UTF-8";

    public String encrypt(String content) {
        try{
            encrypt(content,DEFAULT_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String encrypt(String content, String key) throws Exception {
        if(TextUtils.isEmpty(content))
            return null;
        byte[] contentBytes = content.getBytes(charset);
        byte[] keyBytes = key.getBytes(charset);
        byte[] encryptedBytes = encrypt(contentBytes, keyBytes);
        return Base64.encode(encryptedBytes);
    }

    public String decrypt(String content) {
        try{
            decrypt(content,DEFAULT_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String content, String key) throws Exception {
        if(TextUtils.isEmpty(content))
            return null;
        byte[] encryptedBytes = Base64.decode(content);
        byte[] keyBytes = key.getBytes(charset);
        byte[] decryptedBytes = decrypt(encryptedBytes, keyBytes);
        return new String(decryptedBytes, charset);
    }

    public byte[] encrypt(byte[] contentBytes, byte[] keyBytes) throws Exception {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] contentBytes, byte[] keyBytes) throws Exception {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
    }

    private byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);

        return cipher.doFinal(contentBytes);
    }


}
package com.hlh.util.encryption;


import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static Logger log = LogManager.getLogger(AESUtil.class);

    /**
     * 加密
     *
     * @param sSrc 明文
     * @param sKey 盐
     * @return String 密文
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (!checkKey(sKey)) {
            return null;
        }

        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /**
     * 解密
     *
     * @param sSrc 密文
     * @param sKey 盐
     * @return String 明文
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey) throws Exception {
        if (!checkKey(sKey)) {
            return null;
        }

        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "utf-8");

        return originalString;
    }

    /**
     * 校验数据
     *
     * @param sKey
     * @return
     */
    private static boolean checkKey(String sKey) {
        // 判断Key是否正确
        if (sKey == null) {
            log.info("Key为空null");
            return false;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            log.info("Key长度不是16位");
            return false;
        }
        return true;
    }
}
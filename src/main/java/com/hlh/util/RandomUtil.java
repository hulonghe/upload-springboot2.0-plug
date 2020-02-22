package com.hlh.util;


import com.hlh.util.encryption.AESUtil;
import com.hlh.util.encryption.MD5Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 随机生成工具类
 */

public class RandomUtil {
    public static final RandomUtil dao = new RandomUtil();

    private RandomUtil() {
    }

    /**
     * 生成文件名
     * UUID + 后缀
     *
     * @param fileExtention 文件后缀名
     * @return
     */
    public String getFileName(String fileExtention) {
        return MD5Util.md5(String.valueOf(SnowflakeUtils.dao.nextId())) + "." + fileExtention;
    }

    /**
     * 创建ACCESS_TOKEN
     * MD5
     *
     * @return
     */
    public String createAccessToken() {
        return MD5Util.md5(String.valueOf(SnowflakeUtils.dao.nextId()));
    }

    /**
     * 创建ACCESS_TOKEN
     * AES
     *
     * @param key
     * @return
     */
    public String createAccessToken(String key) {
        try {
            return AESUtil.encrypt(String.valueOf(SnowflakeUtils.dao.nextId()), key);
        } catch (Exception e) {
            e.printStackTrace();
            return createAccessToken();
        }
    }

    /**
     * 将AK进行URLEncode加密
     *
     * @param ak
     * @return
     */
    public String urlEncodeAK(String ak) {
        if (ak == null) {
            return ak;
        }

        try {
            ak = URLEncoder.encode(ak.trim(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ak.replaceAll("/", "+");
        }

        return ak;
    }

}

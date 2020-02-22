package com.hlh.util.convert;

import com.hlh.config.FinalPoolCfg;
import com.hlh.util.encryption.AESUtil;
import com.hlh.util.encryption.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;

/**
 * AttributeConverter: 此接口用于转化实体属性的
 * 用于对密码类型的数据进行加密或者解密
 */

public class PwdEncryptionConvert implements AttributeConverter<String, String> {
    private static Logger log = LoggerFactory.getLogger(PwdEncryptionConvert.class);

    @Override
    public String convertToDatabaseColumn(String value) {
        return passwordEncrypt(value);
    }

    @Override
    public String convertToEntityAttribute(String value) {
        return value;
    }

    public static String passwordEncrypt(String value) {
        try {
            if (value == null || value.length() == 0) {
                return value;
            }

            /* 对已经加过密的密文不再进行加密处理 */
            if (value.length() >= 32) {
                return value;
            }

            String res = AESUtil.encrypt(value, FinalPoolCfg.SECRET_KEY);
            return MD5Util.md5(res);
        } catch (Exception e) {
            log.error("密码:" + value + ",加密失败", e);
        }
        return null;
    }
}

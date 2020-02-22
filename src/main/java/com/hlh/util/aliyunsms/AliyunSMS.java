package com.hlh.util.aliyunsms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送短信实体类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AliyunSMS {

    /**
     * 手机号
     */
    private String phoneNumbers;

    /**
     * 模板参数 格式："{\"code\":\"123456\"}"
     */
    private String templateParam;

    private String outId;

    /**
     * 阿里云模板管理code
     */
    private String templateCode;
}

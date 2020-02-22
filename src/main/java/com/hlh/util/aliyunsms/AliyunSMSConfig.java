package com.hlh.util.aliyunsms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @ClassName: AliyunSMSConfig
 * @Author: Laohu
 * @Description: 阿里云短信配置
 * @Date: 2019/8/6 10:00
 * @Version: 1.0
 */

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliyunSMSConfig {

    /**
     * 阿里云 accessKeyId（安全信息管理下面）
     */
    private String accessKeyId;

    /**
     * 阿里云 accessKeySecret（安全信息管理下面）
     */
    private String accessKeySecret;

    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    private String product;

    /**
     * 产品域名,开发者无需替换
     */
    private String domain;


    private String regionId;

    /**
     * 短信签名名称（国内/国际/港澳台消息-签名管理下面）
     */
    private String signName;

    /**
     * 发送日期 支持30天内记录查询，格式yyyyMMdd
     */
    private String dateFormat;

    /**
     * 服务结点
     */
    private String endpointName;
}

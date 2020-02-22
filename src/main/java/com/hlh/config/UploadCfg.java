package com.hlh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 文件上传配置
 */

@Configuration
@ConfigurationProperties(prefix = "mconfig.multipart")
@Data
public class UploadCfg {
    private boolean resolveLazily;      //resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
    private DataSize maxInMemorySize = DataSize.ofMegabytes(0L);        // 阈值，低于此值，只保留在内存里，超过此阈值，生成硬盘上的临时文件。
    private DataSize maxUploadSize = DataSize.ofMegabytes(1L);          // 上传文件大小

    //显示声明CommonsMultipartResolver为mutipartResolver
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(resolveLazily);
        resolver.setMaxInMemorySize((int) maxInMemorySize.toBytes());
        //上传文件大小 5M 5*1024*1024
        resolver.setMaxUploadSize(maxUploadSize.toBytes());
        return resolver;
    }

}
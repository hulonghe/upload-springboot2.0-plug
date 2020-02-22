package com.hlh.config;

import com.hlh.interceptor.IdentityInterceptor;
import com.hlh.interceptor.SqlInjectInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */

@Configuration
public class InterceptorCfg implements WebMvcConfigurer {

    @Autowired
    private SqlInjectInterceptor sqlInjectInterceptor;

    @Autowired
    private IdentityInterceptor identityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        // 防止SQL注入
//        registry.addInterceptor(sqlInjectInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/static/**"
//                );
//
//        // 权限
//        registry.addInterceptor(identityInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(VarPool.authrityFilterExclude);
    }
}

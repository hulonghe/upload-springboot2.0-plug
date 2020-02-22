package com.hlh.config;

import java.util.HashMap;

import static com.hlh.config.FinalPoolCfg.*;

/**
 * 变量池
 */

public class VarPool {

    /* Yaml配置数据 */
    public static HashMap<String, Object> cfgMap = null;

    /* 不需要经过跨域处理的路径 */
    public static String[] corsFilterExclude = {
            PAGE_FAVICON_URL,
            PAGE_FOUND_NO_URL,
            PAGE_STATIC_URL,
            PAGE_ERR_MSG_URL,                       // 错误信息提示页
            "/error",                               // 错误页
    };

    /* 不需要鉴权的路径 */
    public static String[] authrityFilterExclude = {
            PAGE_FAVICON_URL,
            PAGE_FOUND_NO_URL,
            PAGE_STATIC_URL,
            PAGE_ERR_MSG_URL,                       // 错误信息提示页
            "/login",                               // 登陆验证地址
            "/login.html",                          // 登陆页
            "/register",                            // 注册验证地址
            "/register.html",                       // 注册页
            "/error",                               // 错误页
            "/api/1.0/**",                          // HAL Browser
//            "/**",                                // 全部
    };


}

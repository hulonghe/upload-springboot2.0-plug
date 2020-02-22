package com.hlh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

/**
 * 常量池
 */

@Component
@ConfigurationProperties(prefix = "mconfig.final")
@Data
public class FinalPoolCfg {
    /* 表名 */
    public static final String TB_FILEINFO_NAME = "tbs_fileinfo";
    public static final String TB_USER_NAME = "tbs_user";
    public static final String TB_LOGIN_NAME = "tbs_login";
    public static final String TB_ROLE_NAME = "tbs_role";
    public static final String TB_TYPE_NAME = "tbs_type";
    public static final String TB_ARTICLE_NAME = "tbs_article";
    public static final String TB_FUNCTION_NAME = "tbs_function";
    /* 表视图 */
    public static final String VIEW_ARTICLE_INFO_NAME = "view_article_info";
    public static final String VIEW_LOGIN_USER_ROLE_NAME = "view_login_user_role";


    /* 关系信息 */
    // 类别
    public static final String CFG_INIT_TYPE_KEY = "initEntityData.type";
    // 权限
    public static final String CFG_INIT_ROLE_KEY = "initEntityData.role";
    // 账户
    public static final String CFG_INIT_LOGIN_KEY = "initEntityData.login";
    // ID生成工具类全类名
    public static final String CFG_GENERIC_GENERATOR_ID_PAG_PATH = "com.hlh.util.convert.SnowFlakeIdConvert";

    /* 请求路径信息 */
    // 错误信息提示页
    public static final String PAGE_ERR_MSG_URL = "/err-msg.html";
    // 静态资源路径
    public static final String PAGE_STATIC_URL = "/static/**";
    // 浏览器图标
    public static final String PAGE_FAVICON_URL = "/favicon.ico";
    // 404错误页
    public static final String PAGE_FOUND_NO_URL = "/404.html";

    /* 一些别名信息 */
    // 登陆鉴权需要验证的请求头部字段名
    public static final String HEADER_ACCESS_TOKEN_NAME = "access_token";
    // 存放于当前会话中的登陆信息名
    public static final String SESSION_LOGININFO_NAME = "loginInfo";
    // 存放于当前会话中的错误信息名
    public static final String SESSION_RESCODE_NAME = "resCode";
    // 登录授权-登录参数名
    public static final String PARAM_USERNAME_NAME = "username";
    // 登录授权-口令参数名
    public static final String PARAM_PASSWORD_NAME = "password";

    /* 配置文件 */
    // 加密的盐
    public static String SECRET_KEY;
    //
    public static String SERVER_URL;

    // 当前配置环境
    private String environment;
    // 加密的盐
    private String saltKey;
    // 分页默认参数
    private int page;
    private int size;
    // Win系统下文件保存路径
    private String uploadSavePathWin;
    // Linux系统下文件保存路径
    private String uploadSavePathLinux;
    // Win系统下的logo路径
    private String logoFilePathWin;
    // Linux系统下的logo路径
    private String logoFilePathLinux;
    // 缩略图后缀
    private String imagesSuffix;
    // 图片压缩比例
    private double imageScale;
    // 单张图片超过后启动压缩
    private DataSize imageMoreScale = DataSize.ofKilobytes(300L);
    // 支持上传的图片文件类型
    private String imageExtension;
    // 允许上传的文件类型
    private String fileExtension;
    // 服务器访问根路径
    private String serverUrl;
    // 文件服务器访问根路径
    private String fileServerUrl;
    // 前端web服务访问路径
    private String frontServerUrl;
    // 页面级请求鉴权开关
    private boolean authPageReqStatus;
    // Ajax请求鉴权开关
    private boolean authAjaxReqStatus;

    public void setSaltKey(String saltKey) {
        this.saltKey = saltKey;
        FinalPoolCfg.SECRET_KEY = saltKey;
    }
}

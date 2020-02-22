package com.hlh.util.rep;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应码说明
 */

public class RepCode {
    // 响应体主体
    public static final String REP_CODE = "code";
    public static final String REP_MSG = "msg";
    public static final String REP_DATA = "data";

    // 正常
    public static final int CODE_SUCCESS = 0;
    // 服务繁忙
    public static final int CODE_ERR = 2;
    // 没有数据
    public static final int CODE_NO_DATA = 3;
    // 请求数据校验失败
    public static final int CODE_VALIDATOR_ERR = 7;
    // 用户不存在
    public static final int CODE_NO_USER = 8;
    // 密码错误
    public static final int CODE_PWD_ERR = 9;
    // 账户不可用
    public static final int CODE_USER_STOP = 10;
    // 错误的角色
    public static final int CODE_ROLE_NO = 11;
    // 角色不可用
    public static final int CODE_ROLE_STOP = 12;
    // 登陆失败
    public static final int CODE_LOGIN_ERR = 13;
    // 未登录
    public static final int CODE_LOGIN_NO = 14;
    // 登录已失效
    public static final int CODE_LOGIN_INVAILD_ERR = 15;
    // 未授权
    public static final int CODE_AUTHORIZE_NO = 16;
    // 主键格式错误
    public static final int CODE_ID_ERR = 19;
    // 上传失败，因为文件是空的
    public static final int CODE_UPLOAD_NULL = 20;
    // 上传失败
    public static final int CODE_UPLOAD_ERR = 21;
    // 保存失败
    public static final int CODE_SAVE_ERR = 22;
    // 查询数据失败
    public static final int CODE_FIND_ERR = 23;
    // 请求方式不存在
    public static final int CODE_REQ_METHOD_NO = 24;

    public static Map<Integer, String> msg = new HashMap<>();

    static {
        msg.put(CODE_SUCCESS, "请求成功！");
        msg.put(CODE_ERR, "服务繁忙！");
        msg.put(CODE_NO_DATA, "没有数据！");
        msg.put(CODE_VALIDATOR_ERR, "请求数据校验失败！");
        msg.put(CODE_NO_USER, "用户不存在！");
        msg.put(CODE_PWD_ERR, "密码错误！");
        msg.put(CODE_USER_STOP, "账户不可用！");
        msg.put(CODE_ROLE_NO, "错误的角色！");
        msg.put(CODE_ROLE_STOP, "角色不可用！");
        msg.put(CODE_LOGIN_ERR, "登陆失败！");
        msg.put(CODE_LOGIN_NO, "未登录！");
        msg.put(CODE_ID_ERR, "信息异常！");
        msg.put(CODE_UPLOAD_NULL, "上传失败，因为文件是空的！");
        msg.put(CODE_UPLOAD_ERR, "上传失败！");
        msg.put(CODE_SAVE_ERR, "保存失败！");
        msg.put(CODE_FIND_ERR, "没有可更新数据！");
        msg.put(CODE_LOGIN_INVAILD_ERR, "登录已失效！");
        msg.put(CODE_AUTHORIZE_NO, "未授权！");
        msg.put(CODE_REQ_METHOD_NO, "请求方式不存在！");
    }
}

/**
 * 全局变量
 */
var $; // jquery
var tab; // 标签
// var vueData; // vue初始化参数
var layer; // layui 弹出
var tableId; // 当前活动的数据表单ID
var access_token = '';

var URL_SERVER_ROOT = 'http://127.0.0.1:30002';
var URL_SERVER_INTERFACE_PREFIX = '/api';
var URL_SERVER_INTERFACE_VERSION = '/1.0';
var HEADER_ACCESS_TOKEN_NAME = 'access_token';
var ACCESS_TOKEN_OPERATE = false;

var PAGE_LOGIN_AUTH = '/login';
// 错误信息提示页
var PAGE_ERR_MSG_URL = "/err-msg.html";
// 静态资源路径
var PAGE_STATIC_URL = "/static/**";
// 浏览器图标
var PAGE_FAVICON_URL = "/favicon.ico";

// 不需要登录校验的路径
var AUTHRITY_FILTER_EXCLUDE = [
    PAGE_FAVICON_URL,                       // 静态资源
    PAGE_STATIC_URL,                        // 图标
    PAGE_ERR_MSG_URL,                       // 错误信息提示页
    "/login",                               // 登陆验证地址
    "/login.html",                          // 登陆页
    "/register",                            // 注册验证地址
    "/register.html",                       // 注册页
    "/error",                               // 错误页
    "/",                                    // 首页
];


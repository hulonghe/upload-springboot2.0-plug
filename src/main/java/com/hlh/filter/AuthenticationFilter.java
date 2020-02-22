package com.hlh.filter;

import com.hlh.config.FinalPoolCfg;
import com.hlh.config.VarPool;
import com.hlh.domain.entity.Login;
import com.hlh.domain.entity.Role;
import com.hlh.domain.view.LoginInfoView;
import com.hlh.util.BaseServiceUtil;
import com.hlh.util.CheckUtil;
import com.hlh.util.ReqUtil;
import com.hlh.util.rep.RepCode;
import com.hlh.util.rep.RepUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.hlh.config.FinalPoolCfg.*;

/**
 * 鉴权中心
 * <p>
 * WebFilter 用于将一个类声明为过滤器，该注解将会在部署时被容器处理，容器将根据具体的属性配置将相应的类部署为过滤器。
 * 该注解具有下表给出的一些常用属性，以下所有属性均为可选属性，但是 value、urlPatterns、servletNames
 * 三者必需至少包含一个，且 value 和 urlPatterns 不能共存，如果同时指定，通常忽略 value 的取值
 */

//@WebFilter(filterName = "authenticationFilter", urlPatterns = "/*")
//@Order(99)
@Component
public class AuthenticationFilter extends BaseServiceUtil implements Filter {
    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private FinalPoolCfg finalPoolCfg;


    /**
     * Filter的创建和销毁由WEB服务器负责。
     * web 应用程序启动时，web 服务器将创建Filter 的实例对象，并调用其init方法，完成对象的初始化功能，
     * 从而为后续的用户请求作好拦截的准备工作，filter对象只会创建一次，init方法也只会执行一次。
     * 通过init方法的参数，可获得代表当前filter配置信息的FilterConfig对象。
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
        logger.info("/*==================== AuthenticationFilter init ====================*/");
    }

    /**
     * 用户在配置filter时，可以使用为filter配置一些初始化参数，当web容器实例化Filter对象，
     * 调用其init方法时，会把封装了filter初始化参数的filterConfig对象传递进来。
     * 因此开发人员在编写filter时，通过filterConfig对象的方法，就可获得：
     * <p>
     * String getFilterName()：得到filter的名称。
     * String getInitParameter(String name)： 返回在部署描述中指定名称的初始化参数的值。如果不存在返回null.
     * Enumeration getInitParameterNames()：返回过滤器的所有初始化参数的名字的枚举集合。
     * public ServletContext getServletContext()：返回Servlet上下文对象的引用。
     *
     * @param request
     * @param response
     * @param chain
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = null;
        HttpServletResponse httpServletResponse = null;
        String accept = null;
        if (request instanceof HttpServletRequest) {
            httpServletRequest = (HttpServletRequest) request;

            if(httpServletRequest.getMethod().equals("OPTIONS")){                                    // 排除不排查的请求方式
                chain.doFilter(request, response);
                return;
            }

            /* 请求放行 */
            if ((!finalPoolCfg.isAuthPageReqStatus() && ReqUtil.isPageReq(httpServletRequest)) ||    // 页面级请求
                    !finalPoolCfg.isAuthAjaxReqStatus() && !ReqUtil.isPageReq(httpServletRequest)    // ajax请求
            ) {
                chain.doFilter(request, response);
                return;
            }

            /* 资源放行 */
            if (ReqUtil.isFilterExcludeRequest(httpServletRequest, VarPool.authrityFilterExclude)) {
                chain.doFilter(request, response);
                return;
            }

            logger.info("/*AuthenticationFilter -> doFilter -> " + httpServletRequest.getRequestURI() + "*/");
        }

        if (response instanceof HttpServletResponse) {
            httpServletResponse = (HttpServletResponse) response;
        }

        /* 鉴权 */
        String accessToken = getSessionAK(httpServletRequest);

        boolean bl = CheckUtil.dao.isAccessToken(accessToken);         // 校验结果
        int resCode = RepCode.CODE_SUCCESS;                            // 错误信息
        if (bl) { // 授权码正常
            // 根据授权码获取用户信息
            LoginInfoView loginInfo = getSessionLogin(httpServletRequest, accessToken);

            if (CheckUtil.dao.isLoginInfoEnable(loginInfo)) {           // 有账户信息
                Role role = loginInfo.getRole();
                bl = !(role != null && (role.isDel() || !role.isFlag()));
                if (bl) { // 角色正常
                    Login login = loginInfo.getLogin();
                    if (login.isDel() || !login.isFlag()) {
                        bl = false;
                    } else {
                        bl = true;
                    }
                    if (bl) { // 账户正常
                        /* 整理角色操作权限 */
                        HashMap<String, Boolean> identity = login.getIdentity();
                        // 如果用户访问为空并且角色可访问不为空
                        if (identity == null && role != null && role.getIdentity() != null) {
                            identity = role.getIdentity();
                        }
                        // 如果用户和角色都不为空，则累加
                        else if (identity != null && role != null && role.getIdentity() != null) {
                            identity.putAll(role.getIdentity());
                        }
                        // 避免空异常
                        if (identity == null) {
                            identity = new HashMap<>();
                        }

                        /* 授权校验 */
                        logger.error("/*check auth -> " + httpServletRequest.getRequestURI() + " -> " + identity.toString() + "*/");
                        bl = ReqUtil.isFilterExcludeRequest(httpServletRequest, identity);
                    } else { // 账户不可用
                        resCode = RepCode.CODE_USER_STOP;
                    }
                } else { // 所在角色不可用
                    resCode = RepCode.CODE_ROLE_STOP;
                }
            } else {
                bl = false;
                resCode = RepCode.CODE_LOGIN_INVAILD_ERR;
            }

            /* 回写 */
            if (bl) { // 通过
                logger.error("/*auth success -> " + resCode + " -> " + loginInfo.getLogin().getUsername() + "*/");
                httpServletRequest.getSession().setAttribute(SESSION_LOGININFO_NAME, loginInfo);
                httpServletRequest.getSession().setAttribute(SESSION_RESCODE_NAME, resCode);
                chain.doFilter(request, httpServletResponse);
                return;
            } else {
                resCode = RepCode.CODE_AUTHORIZE_NO;
            }

            /*  重置授权码 */
//                accessToken = RandomUtil.dao.createAccessToken();
//                httpServletResponse.setHeader(FinalPoolCfg.HEADER_ACCESS_TOKEN_NAME, );
        } else {
            resCode = RepCode.CODE_LOGIN_NO;
        }

        logger.error("/*auth fail -> " + resCode + " -> " + RepCode.msg.get(resCode) + "*/");

        /* 未登录，跳转登陆,页面请求 */
        if (resCode == RepCode.CODE_LOGIN_NO &&
                ReqUtil.isPageReq(httpServletRequest)) {
            assert httpServletResponse != null;
            httpServletResponse.sendRedirect(PAGE_ERR_MSG_URL
                    + "?errMsg=" + URLEncoder.encode(RepCode.msg.get(resCode), "utf-8")
                    + "&url=/login.html");
        }

        /* 授权错误，页面请求 */
        else if (resCode != RepCode.CODE_SUCCESS &&
                ReqUtil.isPageReq(httpServletRequest)) {
            assert httpServletResponse != null;
            httpServletResponse.sendRedirect(PAGE_ERR_MSG_URL
                    + "?errMsg=" + URLEncoder.encode(RepCode.msg.get(resCode), "utf-8")
                    + "&url=/login.html");
        }
        /* 授权错误，返回json数据 */
        else if (resCode != RepCode.CODE_SUCCESS) {
            assert httpServletResponse != null;
            RepUtil.writerErrJson(httpServletRequest, httpServletResponse, resCode);
        }
    }

    /**
     * Web容器调用destroy方法销毁Filter。destroy方法在Filter的生命周期中仅执行一次。
     * 在destroy方法中，可以释放过滤器使用的资源。
     */
    @Override
    public void destroy() {
        // do nothing
        logger.info("/*==================== AuthenticationFilter destroy ====================*/");
    }
}
package com.hlh.util;

import com.hlh.config.FinalPoolCfg;
import com.hlh.domain.view.LoginInfoView;
import com.hlh.util.rep.RepCode;
import com.hlh.util.rep.RepUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.hlh.config.FinalPoolCfg.HEADER_ACCESS_TOKEN_NAME;

@Component
public class BaseServiceUtil {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private RedisUtil redisUtil;

    @Value("${mconfig.redis.accessTokenTimeout}")
    private long akTimeout;

    /**
     * 保存登陆状态
     *
     * @param request
     * @param response
     * @param loginInfo
     */
    public boolean setSessionLogin(HttpServletRequest request, HttpServletResponse response, LoginInfoView loginInfo) {
        /* 授权 */
        String accessToken = RandomUtil.dao.createAccessToken(FinalPoolCfg.SECRET_KEY);
        accessToken = RandomUtil.dao.urlEncodeAK(accessToken);

        response.setHeader(FinalPoolCfg.HEADER_ACCESS_TOKEN_NAME, accessToken);

        /* 保存登录信息 */
        HttpSession session = request.getSession();
        session.setAttribute(FinalPoolCfg.SESSION_LOGININFO_NAME, loginInfo);
        session.setAttribute(FinalPoolCfg.SESSION_RESCODE_NAME, RepCode.CODE_SUCCESS);
        session.setAttribute(FinalPoolCfg.HEADER_ACCESS_TOKEN_NAME, accessToken);

        logger.info("/* 保存登陆信息 -> " + loginInfo.getLogin().getUsername() + " -> " + RepCode.CODE_SUCCESS + " -> " + accessToken + "*/");
        logger.info("/* loginInfo ->  " + loginInfo + "*/");

        return redisUtil.set(accessToken, loginInfo, akTimeout);
    }

    /**
     * 登出
     *
     * @param request
     * @param ak
     * @return
     */
    public boolean removeSossionLogin(HttpServletRequest request, String ak) {
        if (!CheckUtil.dao.isAccessToken(ak)) {
            ak = getSessionAK(request);
        }

        if (CheckUtil.dao.isAccessToken(ak)) {
            request.getSession().removeAttribute(FinalPoolCfg.SESSION_LOGININFO_NAME);
            request.getSession().removeAttribute(FinalPoolCfg.SESSION_RESCODE_NAME);
            request.getSession().removeAttribute(FinalPoolCfg.HEADER_ACCESS_TOKEN_NAME);
            redisUtil.remove(ak.trim());

            logger.info("/* 已登出 -> " + ak + "*/");

            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取授权码
     *
     * @param request
     * @return
     */
    public String getSessionAK(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String ak = (String) session.getAttribute(FinalPoolCfg.HEADER_ACCESS_TOKEN_NAME);

        // 请求头中获取
        if (!CheckUtil.dao.isAccessToken(ak)) {
            ak = request.getHeader(HEADER_ACCESS_TOKEN_NAME);
        }

        // 请求路径中获取
        if (!CheckUtil.dao.isAccessToken(ak)) {
            ak = request.getParameter(FinalPoolCfg.HEADER_ACCESS_TOKEN_NAME);
            ak = RandomUtil.dao.urlEncodeAK(ak);
        }

        logger.info("/*req -> " + request.getRequestURL() + " -> " + request.getMethod() + " -> AK -> " + ak + "*/");

        return ak == null ? ak : ak.trim();
    }

    /**
     * 获取登陆信息
     *
     * @param request
     * @param ak
     * @return
     */
    public LoginInfoView getSessionLogin(HttpServletRequest request, String ak) {
        if (!CheckUtil.dao.isAccessToken(ak)) {
            ak = getSessionAK(request);
        }

        if (!CheckUtil.dao.isAccessToken(ak)) {
            return null;
        }

        /* 取授权信息 */
        HttpSession session = request.getSession();
        LoginInfoView loginInfo = (LoginInfoView) session.getAttribute(FinalPoolCfg.SESSION_LOGININFO_NAME);

        if (loginInfo == null) {
            loginInfo = (LoginInfoView) redisUtil.get(ak.trim());
        }

        return loginInfo;
    }

    /**
     * 错误响应处理，公共方法
     *
     * @param request
     * @param errorCode
     * @return
     */
    public Object error(HttpServletRequest request, int errorCode) {
        logger.error("/*response error -> " + request.getRequestURL() + " -> " + request.getMethod() + " -> " + errorCode + "*/");

        String accept = request.getHeader("accept");
        if (accept.contains("text/html")) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("404");
            return mav;
        } else {
            return RepUtil.post(errorCode);
        }
    }

    /**
     * 流参数校验失败错误响应
     *
     * @param request
     * @param bindingResult
     * @return
     */
    public Object error(HttpServletRequest request, BindingResult bindingResult) {
        logger.error("/*request param error -> " + request.getRequestURL() + " -> " + request.getMethod()
                + " -> " + bindingResult.getFieldError().getField() + " -> "
                + bindingResult.getFieldError().getDefaultMessage() + "*/");

        String accept = request.getHeader("accept");
        if (accept.contains("text/html")) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("404");
            return mav;
        } else {
            return RepUtil.post(bindingResult.getFieldError());
        }
    }
}

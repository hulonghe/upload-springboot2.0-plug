package com.hlh.filter;

import com.hlh.config.VarPool;
import com.hlh.util.BaseServiceUtil;
import com.hlh.util.ReqUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 防SQL注入，XSS
 * <p>
 * WebFilter 用于将一个类声明为过滤器，该注解将会在部署时被容器处理，容器将根据具体的属性配置将相应的类部署为过滤器。
 * 该注解具有下表给出的一些常用属性，以下所有属性均为可选属性，但是 value、urlPatterns、servletNames
 * 三者必需至少包含一个，且 value 和 urlPatterns 不能共存，如果同时指定，通常忽略 value 的取值
 */

//@WebFilter(filterName = "sqlInjectFilter", urlPatterns = "/*")
//@Order(1)
@Component
public class SqlInjectFilter extends BaseServiceUtil implements Filter {
    Logger logger = LogManager.getLogger(this.getClass());

    private static String reg = "(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|" +
            "substr|ascii|declare|exec|count|master|into|drop|execute|chr|mid|script|" +
            "frame|etc|style|expression)\\b)";

    private static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

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
        logger.info("/*==================== SqlInjectFilter init ====================*/");
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
     * @param servletRequest
     * @param servletResponse
     * @param chain
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = null;
        HttpServletResponse response = null;

        if (servletRequest instanceof HttpServletRequest) {
            request = (HttpServletRequest) servletRequest;
        }
        if (servletResponse instanceof HttpServletResponse) {
            response = (HttpServletResponse) servletResponse;
        }

        if (request != null && response != null) {
            if (!response.isCommitted()) {
                if (request.getMethod().equals("OPTIONS")) { // 过滤OPTIONS请求
                    chain.doFilter(request, response);
                    return;
                }

                /* 资源放行 */
                if (ReqUtil.isFilterExcludeRequest(request, VarPool.corsFilterExclude)) {
                    chain.doFilter(request, response);
                    return;
                }

                logger.info("/*SqlInjectFilter -> doFilter -> " + request.getRequestURI() + "*/");

                /* 校验页参 */
                Enumeration<String> names = request.getParameterNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    String[] values = request.getParameterValues(name);
                    for (String value : values) {
                        logger.info("/*page param -> " + name + " -> " + value + "*/");
                        check(name, value);
                    }
                }

                /* 如果不是页面级请求，则同时校验流参数 */
                if (!ReqUtil.isPageReq(request)) {
                    Map paramsFromRequestBody = ReqUtil.getParamsFromRequestBody(request);
                    for (Object key : paramsFromRequestBody.keySet()) {
                        logger.info("/*flow param -> " + key + " -> " + paramsFromRequestBody.get(key) + "*/");
                        check(key.toString(), paramsFromRequestBody.get(key).toString());
                    }
                }
            }

            chain.doFilter(request, response);
        }
    }

    /**
     * Web容器调用destroy方法销毁Filter。destroy方法在Filter的生命周期中仅执行一次。
     * 在destroy方法中，可以释放过滤器使用的资源。
     */
    @Override
    public void destroy() {
        logger.info("/*==================== SqlInjectFilter destroy ====================*/");
    }

    public void check(String name, String value) throws IOException {
        boolean bl = true;
        value = clearXss(value);
        value = value.toLowerCase();
        bl = isValid(value);
        if (!bl) {
            logger.info("/*invalid page param -> " + name + " -> " + value + " -> " + bl + "*/");
            throw new IOException("invalid page param");
        }
    }

    /**
     * SQL关键词校验
     *
     * @param str
     * @return
     */
    public boolean isValid(String str) {
        try {
            if (sqlPattern.matcher(str).find()) {
                logger.error("未能通过过滤器：str=" + str);
                return false;
            }
        } catch (Exception e) {
            logger.error("未能通过过滤器：str=" + str, e);
            return false;
        }
        return true;
    }

    /**
     * XSS 过滤
     *
     * @param value
     * @return
     */
    private String clearXss(String value) {
        if (value == null || "".equals(value)) {
            return value;
        }
        value = value.replaceAll("<", "<").replaceAll(">", ">");
        value = value.replaceAll("\\(", "(").replace("\\)", ")");
        value = value.replaceAll("'", "'");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
                "\"\"");
        value = value.replace("script", "");
        return value;
    }
}

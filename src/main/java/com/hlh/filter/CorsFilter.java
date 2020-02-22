package com.hlh.filter;

import com.hlh.config.VarPool;
import com.hlh.util.ReqUtil;
import com.hlh.util.rep.RepUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 浏览器跨域请求处理
 * <p>
 * WebFilter 用于将一个类声明为过滤器，该注解将会在部署时被容器处理，容器将根据具体的属性配置将相应的类部署为过滤器。
 * 该注解具有下表给出的一些常用属性，以下所有属性均为可选属性，但是 value、urlPatterns、servletNames
 * 三者必需至少包含一个，且 value 和 urlPatterns 不能共存，如果同时指定，通常忽略 value 的取值
 */

//@WebFilter(filterName = "corsFilter2", urlPatterns = "/*")
//@Order(2)
@Component
public class CorsFilter implements Filter {
    Logger logger = LogManager.getLogger(this.getClass());

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
        logger.info("/*==================== CorsFilter init ====================*/");
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            HttpServletRequest httpServletRequest  = null;
            if (request instanceof HttpServletRequest) {
                httpServletRequest = (HttpServletRequest) request;

                /* 资源放行 */
                if (ReqUtil.isFilterExcludeRequest(httpServletRequest, VarPool.corsFilterExclude)) {
                    chain.doFilter(request, response);
                    return;
                }
            }

            HttpServletResponse httpServletResponse = null;
            if (response instanceof HttpServletResponse) {
                httpServletResponse = (HttpServletResponse) response;
                RepUtil.setCors(httpServletRequest, httpServletResponse);
            }

            if (httpServletResponse != null) {
                chain.doFilter(request, httpServletResponse);
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            logger.error("CorsFilter fail -> doFilter -> " + e.getMessage(), e);
        }
    }

    /**
     * Web容器调用destroy方法销毁Filter。destroy方法在Filter的生命周期中仅执行一次。
     * 在destroy方法中，可以释放过滤器使用的资源。
     */
    @Override
    public void destroy() {
        logger.info("/*==================== CorsFilter destroy ====================*/");
    }
}
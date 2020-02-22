package com.hlh.interceptor;

import com.hlh.config.FinalPoolCfg;
import com.hlh.util.BaseServiceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访问授权
 */

@Component
public class IdentityInterceptor extends BaseServiceUtil implements HandlerInterceptor {
    private Logger logger = LogManager.getLogger(this.getClass());


    /**
     * 调用时间：Controller方法处理之前
     * 执行顺序：链式Intercepter情况下，Intercepter按照声明的顺序一个接一个执行
     * 若返回false，则中断执行，注意：不会进入afterCompletion
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object object) throws Exception {
        try {

            if (!response.isCommitted()) {
                if (request.getMethod().equals("OPTIONS")) { // 过滤OPTIONS请求
                    return false;
                }

                logger.info("/*IdentityInterceptor -> " + request.getRequestURI() + " -> " + request.getSession().getAttribute(FinalPoolCfg.SESSION_RESCODE_NAME) + "*/");
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 调用前提：preHandle返回true
     * 调用时间：Controller方法处理完之后，DispatcherServlet进行视图的渲染之前，也就是说在这个方法中你可以对ModelAndView进行操作
     * 执行顺序：链式Intercepter情况下，Intercepter按照声明的顺序倒着执行。
     * 备注：postHandle虽然post打头，但post、get方法都能处理
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object object, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 调用前提：preHandle返回true
     * 调用时间：DispatcherServlet进行视图的渲染之后
     * 备注: 多用于清理资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object object, Exception exception)
            throws Exception {

    }
}

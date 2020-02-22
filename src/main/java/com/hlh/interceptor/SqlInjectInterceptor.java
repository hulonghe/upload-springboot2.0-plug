package com.hlh.interceptor;

import com.hlh.util.ReqUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 防SQL注入
 */

@Component
public class SqlInjectInterceptor implements HandlerInterceptor {
    private Logger logger = LogManager.getLogger(this.getClass());

    private static String reg = "(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|" +
            "substr|ascii|declare|exec|count|master|into|drop|execute|chr|mid|script|" +
            "frame|etc|style|expression)\\b)";

    private static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

    /**
     * 调用时间：Controller方法处理之前
     * 执行顺序：链式Intercepter情况下，Intercepter按照声明的顺序一个接一个执行
     * 若返回false，则中断执行，注意：不会进入afterCompletion
     */
    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                             Object arg2) {
        try {
            if (!arg1.isCommitted()) {
                if (arg0.getMethod().equals("OPTIONS")) { // 过滤OPTIONS请求
                    return false;
                }
                logger.info("/*SqlInjectInterceptor -> " + arg0.getRequestURI() + "*/");

                boolean bl = true;
                Enumeration<String> names = arg0.getParameterNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    String[] values = arg0.getParameterValues(name);
                    for (String value : values) {
                        value = clearXss(value);
                        value = value.toLowerCase();
                        logger.info("/*request param -> " + name + " -> " + value + "*/");
                        bl = isValid(value);
                        if (!bl) {
                            logger.info("/*invalid request param -> " + name + " -> " + value + " -> " + bl + "*/");
                            return false;
                        }
                    }
                }

                if (!"GET".equals(arg0.getMethod())) {
                    Map paramsFromRequestBody = ReqUtil.getParamsFromRequestBody(arg0);
                    for (Object key : paramsFromRequestBody.keySet()) {
                        logger.info("/*flow param -> " + key + " -> " + paramsFromRequestBody.get(key) + "*/");
                    }
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 调用前提：preHandle返回true
     * 调用时间：Controller方法处理完之后，DispatcherServlet进行视图的渲染之前，也就是说在这个方法中你可以对ModelAndView进行操作
     * 执行顺序：链式Intercepter情况下，Intercepter按照声明的顺序倒着执行。
     * 备注：postHandle虽然post打头，但post、get方法都能处理
     */
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {
        if (!arg1.isCommitted()) {
//            logger.info("/*视图渲染之前: " + arg0.getSession().getId() + "*/");
        }
    }

    /**
     * 调用前提：preHandle返回true
     * 调用时间：DispatcherServlet进行视图的渲染之后
     * 备注: 多用于清理资源
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        if (!arg1.isCommitted()) {
//            logger.info("/*视图渲染后: " + arg0.getSession().getId() + "*/");
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

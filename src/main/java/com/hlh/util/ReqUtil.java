package com.hlh.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 请求体工具类
 */

public class ReqUtil {

    private static Logger logger = LogManager.getLogger(ReqUtil.class);

    /**
     * 获取请求体
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request;
    }

    /**
     * 获取响应体
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        return response;
    }

    /**
     * 页面级请求验证
     *
     * @param request
     * @return
     */
    public static boolean isPageReq(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        return accept != null && accept.contains("text/html");
    }

    public static String getRootUrl(HttpServletRequest request) {
        String name = request.getParameter("action");
        if (name.indexOf("!") != -1) {
            String targetMethod = name.substring(0, name.indexOf("!"));
            name = name.replace(name, targetMethod);
        }
        String url = getURI(request) + "?" + "action" + "=" + name;
        if (logger.isDebugEnabled()) {
            logger.debug("root url:" + url);
        }
        return url;
    }

    public static String getRealUrl(HttpServletRequest request) {
        String name = request.getParameter("action");
        String queryString = request.getQueryString();
        if (name.indexOf("!") != -1) {
            String targetMethod = name.substring(name.indexOf("!") + 1);
            queryString = queryString.replace(name, targetMethod);
        }
        String url = getURI(request) + "?" + queryString;
        if (logger.isDebugEnabled()) {
            logger.debug("real url:" + url);
        }
        return url;
    }

    public static String getFullURL(HttpServletRequest request) {
        String uri = getURI(request);
        String queryString = request.getQueryString();
        String url;
        if (queryString == null || "".equals(queryString.trim())) {
            url = uri;
        } else {
            url = uri + "?" + queryString;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("full url:" + url);
        }
        return url;
    }

    public static String getURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * 获取当前模块请求路径前缀
     *
     * @param request
     * @return
     */
    public static String getModulePath(HttpServletRequest request) {
        String url = getFullURL(request);
        String contextPath = request.getContextPath();
        return getModulePath(url, contextPath);
    }

    public static String getModulePath(String url, String contextPath) {
        url = url.replace(contextPath, "");
        int index = url.indexOf("/", 1);
        String modulePath = "";
        if (index != -1) {
            modulePath = url.substring(0, index);
        } else {
            return null;
        }
        return contextPath + modulePath;
    }

    /**
     * 获取请求IP地址
     *
     * @param request
     * @return
     */
    public static String getIpV4(HttpServletRequest request) {
        return NetUtil.getIpAddr(request);
    }

    /**
     * 获取请求体内容
     *
     * @return
     * @throws IOException
     */
    public static Map getParamsFromRequestBody(HttpServletRequest request) {
        BufferedReader reader = null;
        try {
            reader = request.getReader();
        } catch (IOException e) {
            return new HashMap<>();
        }

        StringBuilder builder = new StringBuilder();
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String bodyString = builder.toString();
            if (bodyString != null && bodyString.length() > 2) {
                JSONObject jsonObject = JSON.parseObject(bodyString);
                Map<String, Object> map = jsonObject;
                return map;
            } else {
                return new HashMap();
            }
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

    /**
     * 判断请求路径是否存在于excludeStr规则中
     *
     * @param request    请求体
     * @param excludeStr 路径规则
     * @return
     */
    public static boolean isFilterExcludeRequest(HttpServletRequest request, Iterable<String> excludeStr) {
        String url = request.getRequestURI();
        for (String ex : excludeStr) {
            if (isFilterExcludeRequest(url, ex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 资源放行
     *
     * @param request
     * @param excludeStr
     * @return
     */
    public static boolean isFilterExcludeRequest(HttpServletRequest request, String[] excludeStr) {
        String url = request.getRequestURI();
        for (String ex : excludeStr) {
            if (isFilterExcludeRequest(url, ex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 资源放行
     *
     * @param request
     * @param excludeStr
     * @return
     */
    public static boolean isFilterExcludeRequest(HttpServletRequest request, HashMap<String, Boolean> excludeStr) {
        String url = request.getRequestURI();
        Set<String> strings = excludeStr.keySet();
        for (String ex : strings) {
            if (excludeStr.get(ex) && isFilterExcludeRequest(url, ex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 资源放行
     *
     * @param url
     * @param ex
     * @return
     */
    public static boolean isFilterExcludeRequest(String url, String ex) {
        /* 空数据校验 */
        if (!CheckUtil.dao.isStrNotNull(ex)) {
            return false;
        }

        /* 相等则直接放行 */
        if (url.equals(ex)) {
            return true;
        }

        /* 前缀部分相等则放行 */
        int index = ex.indexOf("/**");
        if (index != -1 && url.length() >= index) {
            if (url.substring(0, index).equals(ex.substring(0, index))) {
                return true;
            }
        }

        /* 后缀部分相等则放行 */
        index = ex.indexOf("**/");
        if (index != -1 && url.length() >= (index + 3)) {
            index += 3;
            if (url.substring(index).equals(ex.substring(index))) {
                return true;
            }
        }

        return false;
    }

}
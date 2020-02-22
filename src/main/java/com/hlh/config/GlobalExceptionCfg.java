package com.hlh.config;

import com.hlh.util.rep.RepCode;
import com.hlh.util.rep.RepUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * 全局异常捕捉处理类
 */

@ControllerAdvice
public class GlobalExceptionCfg {
    private Logger log = LogManager.getLogger(this.getClass());

    /**
     * 统一异常处理，Json返回的
     *
     * @param request   请求实体
     * @param exception 异常
     * @return 返回JSON字符串或者error页面
     */
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public Object defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
//        log.error(exception.getMessage(), exception);
        exception.printStackTrace();

        // 异常堆信息，主要是日志跟踪
        String errorMsg = exception.getMessage();
        // 请求的URL
        String reqUrl = request.getRequestURL().toString();

        // 前台传递过来的参数
        Enumeration<String> ent = request.getParameterNames();
        List<String> sbParaList = new ArrayList<>();
        while (ent.hasMoreElements()) {
            String paraName = ent.nextElement();
            String paraVal = request.getParameter(paraName);
            sbParaList.add(paraName + "=" + paraVal);
        }

        // 请求的参数，多个以逗号隔开
        String reqParas = "该请求没有参数";
        if (sbParaList.size() > 0) {
            reqParas = StringUtils.join(sbParaList.toArray(), ",");
        }

        String msg = "{url: '" + reqUrl + "', param: '" + reqParas + "', method: '" + request.getMethod() + "'}";
        log.error("请求响应失败: " + msg);

        String accept = request.getHeader("accept");
        if (accept != null && !accept.contains("text/html")) {
            return getErrMsg(response, exception, errorMsg, msg);
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("404");
            return mav;
        }
    }

    /**
     * 根据情况给出不同的错误响应
     *
     * @param response
     * @param e
     * @param errorMsg
     * @param msg
     * @return
     */
    private Object getErrMsg(HttpServletResponse response, Throwable e, String errorMsg, String msg) {
        String exClassName = e.getClass().getName();

        /* 遍历错误信息 */
        Throwable cause = e.getCause();
        while (cause != null) {
            /* 流参数异常-不符合规定 */
            if ((cause instanceof ConstraintViolationException)) {
                exClassName = cause.getClass().getName();
                break;
            }
            cause = cause.getCause();
        }

        /* 根据情况给出响应 */
        switch (exClassName) {
            case "org.springframework.data.rest.webmvc.ResourceNotFoundException":  // REST DATA 数据不存在
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                return RepUtil.post(RepCode.CODE_NO_DATA);
            case "javax.validation.ConstraintViolationException":                   // 实体属性值校验失败
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) cause;
                Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
                LinkedList<Map<String, String>> objects = new LinkedList<>();
                for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                    Map<String, String> map = new HashMap<>();
                    map.put("field", constraintViolation.getPropertyPath().toString());
                    map.put("errMsg", constraintViolation.getMessageTemplate());
                    objects.add(map);
                }
                return RepUtil.post(RepCode.CODE_VALIDATOR_ERR, objects.get(0).get("errMsg"), objects);
            case "org.springframework.web.HttpRequestMethodNotSupportedException":  // 请求方式不存在
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return RepUtil.post(RepCode.CODE_REQ_METHOD_NO);
            default:
                return RepUtil.post(RepCode.CODE_ERR, errorMsg, msg);
        }
    }
}

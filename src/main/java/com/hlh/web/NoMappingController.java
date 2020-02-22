package com.hlh.web;

import com.hlh.util.rep.RepCode;
import com.hlh.util.rep.RepUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求不存在的处理
 */

@Controller
public class NoMappingController implements ErrorController {

    private Logger log = LogManager.getLogger(this.getClass());

    /**
     * 请求不存在的处理
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/error")
    public Object handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String method = request.getMethod();
        String accept = request.getHeader("accept");

        if (accept != null && !accept.contains("text/html")) {
            return RepUtil.post(RepCode.CODE_ERR, "错误的" + method + "请求");
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("404");
            return mav;
        }
    }

    /**
     * 该方法和以上方法不能同时共存，因为@RequestMapping("/error")相同
     * 异常的统一处理
     * 是有的错误都到这个页面
     *
     * @return
     */
    @Override
    @RequestMapping("/404.html")
    public String getErrorPath() {
        return "404";
    }
}

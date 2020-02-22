package com.hlh.web;

import com.hlh.config.FinalPoolCfg;
import com.hlh.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录、注册、授权
 */

@Controller
public class AuthorizeController {

    @Autowired
    private AuthorizeService authorizeService;

    /* 登录页 */
    @GetMapping({"/login.html", "/login"})
    public String pageLogin(
            HttpServletRequest request,
            @RequestParam(value = FinalPoolCfg.HEADER_ACCESS_TOKEN_NAME, required = false) String ak
    ) {
        authorizeService.loginOut(request, ak);
        return "login";
    }

    /* 登陆授权 */
    @PostMapping({"/login.html", "/login"})
    @ResponseBody
    public Object login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Map param
    ) {
        return authorizeService.login(request, response, param);
    }

    /* 注册页 */
    @GetMapping({"/register.html", "/register"})
    public String pageRegister() {
        return "register";
    }

    /* 注册验证 */
    @PostMapping({"/register.html", "/register"})
    @ResponseBody
    public Object register(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Map param
    ) {
        return authorizeService.register(request, response, param);
    }
}

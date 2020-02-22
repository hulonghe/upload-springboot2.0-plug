package com.hlh.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.hlh.config.FinalPoolCfg.PAGE_ERR_MSG_URL;

@Controller
public class PagesExceptionController {

    @GetMapping("404.html")
    public String pageNoFound() {
        return "404";
    }

    @GetMapping(PAGE_ERR_MSG_URL)
    public String errMsg() {
        return "err_msg";
    }
}

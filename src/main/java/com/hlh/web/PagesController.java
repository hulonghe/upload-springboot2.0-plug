package com.hlh.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping({"/admin/", "/admin/index", "/admin/index.html"})
    public String index() {
        return "admin/index";
    }

    @GetMapping("/common/menu_left.html")
    public String menu_left() {
        return "common/menu_left";
    }

    @GetMapping("/common/menu_top.html")
    public String menu_top() {
        return "common/menu_top";
    }

    @GetMapping("/common/main.html")
    public String main() {
        return "common/main";
    }

    @GetMapping("/common/foot.html")
    public String foot() {
        return "common/foot";
    }

    @GetMapping("/admin/auth-user.html")
    public String user() {
        return "admin/user";
    }

    @GetMapping("/admin/article.html")
    public String article() {
        return "admin/article";
    }
}

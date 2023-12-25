package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.service.UserService;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @RequestMapping("/auth")
    public String form(Model model) {
        model.addAttribute("message", "Hello");
        return "auth/index";
    }

    @RequestMapping("/auth/login/success")
    public String success(Model model) {
        model.addAttribute("message", "Đăng nhập thành công");

        return "forward:/auth";
    }

    @RequestMapping("/auth/login/error")
    public String error(Model model) {
        model.addAttribute("message", "Sai thông tin đăng nhập");

        return "forward:/auth";
    }

    @RequestMapping("/auth/logoff/success")
    public String logoff(Model model) {
        model.addAttribute("message", "Đăng xuất thành công");

        return "forward:/auth";
    }

    @RequestMapping("/auth/access/denied")
    public String denied(Model model) {
        model.addAttribute("message", "Bạn không có quyền truy xuất");

        return "forward:/auth";
    }

}

package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.poly.bean.User;
import com.poly.service.UserService;

@Controller
public class TestEncodeController {
    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder pe;

    @GetMapping("/api/products/encode")
    public String encode(Model model) {
        List<User> users = userService.findAll();
        for (User user : users) {
            System.out.println(user.getId() + ": " + pe.encode(user.getPassword()));
        }
        return "";
    }
}

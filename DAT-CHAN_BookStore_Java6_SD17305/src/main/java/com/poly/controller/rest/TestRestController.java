package com.poly.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.bean.User;
import com.poly.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class TestRestController {

    @Autowired
    UserService userService;

    @GetMapping("/test")
    public List<User> getUsers() {
        return userService.findAll();
    }
}

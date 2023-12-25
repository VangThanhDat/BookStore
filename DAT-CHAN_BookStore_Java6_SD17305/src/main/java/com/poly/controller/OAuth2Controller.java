package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.service.UserLoginService;
import com.poly.service.UserService;

@Controller
public class OAuth2Controller {

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    UserService userService;

    @RequestMapping("/oauth2/login/success")
    public String success(OAuth2AuthenticationToken oauth2) {
        userLoginService.loginFromOAuth2(oauth2);
        String name = oauth2.getPrincipal().getAttribute("name");
        String username = name.toLowerCase().replaceAll("\\s+", "");
        String id = userService.findById(username).getId();
        String email = userService.findById(username).getEmail();
        return "redirect:/#!/?id=" + id + "&email=" + email;
    }
}

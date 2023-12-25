package com.poly.controller.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.bean.User;
import com.poly.service.UserService;
import com.poly.util.JWTTokenUtil;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class OAuth2RestController {
    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JWTTokenUtil jwtTokenUtil;

    @Autowired
    HttpSession session;

    @PostMapping("/oauth2/user/login")
    public ResponseEntity<?> getUserGoogle(@RequestBody Map<String, String> map) {
        System.out.println("User: " + map.get("id"));
        if (userService.existsById(map.get("id")) &&
                userService.existsByEmail(map.get("email"))) {
            User userLogin = userService.findById(map.get("id"));
            System.out.println("User login: " + userLogin);
            try {
                // Xác thực thông tin đăng nhập sử dụng AuthenticationManager
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userLogin.getId(),
                                userLogin.getPassword()));
                System.out.println("Controller Login " + authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());

                // Tạo JWT token
                String jwtToken = jwtTokenUtil.generateToken(authentication);
                String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);

                // Lấy thông tin đăng nhập
                User userL = userService.findById(jwtTokenUtil.getUsernameFromToken(jwtToken));

                // Trả về phản hồi thành công với JWT token trong header "Authorization"
                Map<String, Object> response = new HashMap<>();
                response.put("name", userL.getFullname());

                response.put("message", "Đăng nhập thành công");
                response.put("token", jwtToken);
                response.put("refreshToken", refreshToken);
                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + jwtToken)
                        .body(response);
            } catch (AuthenticationException e) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Đăng nhập thất bại: Mật khẩu không chính xác");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đăng nhập thất bại: Tên đăng nhập không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}

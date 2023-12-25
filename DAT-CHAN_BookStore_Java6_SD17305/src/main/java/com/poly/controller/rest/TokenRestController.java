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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.bean.User;
import com.poly.repository.UserRepository;
import com.poly.service.UserLoginService;
import com.poly.service.UserService;
import com.poly.util.JWTTokenUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class TokenRestController {
    @Autowired
    JWTTokenUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    HttpSession session;

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping("/getNewToken")
    public ResponseEntity<?> getToken(@RequestBody Map<String, String> tokenMap) {
        String token = tokenMap.get("token");
        System.out.println("token old: " + token);
        if (token != null && !token.isEmpty()) {
            // Kiểm tra và xác thực token cũ
            if (jwtTokenUtil.validateToken(token)) {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                UserDetails userDetails = userLoginService.loadUserByUsername(username);

                // Tạo đối tượng Authentication từ UserDetails
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Đặt đối tượng Authentication vào SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Tạo token mới
                String newToken = jwtTokenUtil.generateToken(authentication);

                // Trả về token mới
                Map<String, Object> response = new HashMap<>();
                response.put("token", newToken);
                System.out.println("new Token " + newToken);
                return ResponseEntity.ok(response);

            } else {
                // Token không hợp lệ
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            // Không có token hoặc token trống
            return ResponseEntity.badRequest().build();
        }
    }

}

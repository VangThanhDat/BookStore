package com.poly.util;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTTokenUtil {

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey; // Key bí mật, bạn nên thay đổi và bảo mật nó

    @Value("${jwt.expiration}")
    private long validityInMilliseconds; // Thời gian hết hạn token (1 phút)

    public String generateToken(Authentication authentication) {
        System.out.println(authentication.getPrincipal().toString());
        System.out.println(authentication.getName());
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + validityInMilliseconds);
        // Định nghĩa các claims (thuộc tính) của JWT token
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        List<String> listR = userRepository.findRoleIdsByUsername(authentication.getName());
        claims.put("roles", listR.toArray(new String[0])); // Thêm thông tin về vai trò (roles)

        Map<String, Object> customHeader = new HashMap<>();
        customHeader.put("alg", "HS256");
        customHeader.put("typ", "JWT");

        return Jwts.builder()
                .setHeader(customHeader)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        System.out.println(authentication.getPrincipal().toString());
        System.out.println(authentication.getName());
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + validityInMilliseconds * 12 * 24);
        // Định nghĩa các claims (thuộc tính) của JWT token
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        List<String> listR = userRepository.findRoleIdsByUsername(authentication.getName());
        claims.put("roles", listR.toArray(new String[0])); // Thêm thông tin về vai trò (roles)

        Map<String, Object> customHeader = new HashMap<>();
        customHeader.put("alg", "HS256");
        customHeader.put("typ", "JWT");

        return Jwts.builder()
                .setHeader(customHeader)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return (List<String>) claims.get("roles");
    }

    public String getPasswordFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("password");
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }

    // public Date getExpirationFromToken(String token) {
    // Claims claims = extractClaims(token);
    // return claims.getExpiration();
    // }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public void handleResponseTokenError(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {
        // Tạo đối tượng chứa thông tin lỗi
        Map<String, Object> errorObject = new HashMap<>();
        errorObject.put("status", status.value());
        errorObject.put("message", message);

        // Chuyển đối tượng lỗi thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(errorObject);

        // Ghi dữ liệu JSON vào luồng phản hồi
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(jsonString);
    }

}

package com.poly.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.poly.service.UserLoginService;
import com.poly.util.JWTTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JWTTokenUtil jwtTokenUtil;

    private final UserLoginService userLoginService;

    public JwtTokenFilter(JWTTokenUtil jwtTokenUtil, UserLoginService userLoginService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userLoginService = userLoginService;
    }

    // private static final Set<String> IGNORED_URIS = new HashSet<String>();

    // static {
    // IGNORED_URIS.add("/api/login");
    // IGNORED_URIS.add("/api/register");
    // IGNORED_URIS.add("/api/logout/success");
    // IGNORED_URIS.add("/oauth2/login/success");
    // IGNORED_URIS.add("/api/oauth2");
    // // IGNORED_URIS.add("/api/products");
    // // IGNORED_URIS.add("/api/category");
    // // IGNORED_URIS.add("/api/category/null/products");
    // // IGNORED_URIS.add("/api/category/1/products");
    // // IGNORED_URIS.add("/api/category/2/products");
    // // IGNORED_URIS.add("/api/category/3/products");
    // // IGNORED_URIS.add("/api/category/4/products");
    // // IGNORED_URIS.add("/api/category/5/products");
    // // IGNORED_URIS.add("/api/category/6/products");
    // IGNORED_URIS.add("/");
    // }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, JwtException, ExpiredJwtException {
        System.out.println("Filter Enable");

        String requestURI = request.getRequestURI();
        String requestURL = request.getRequestURL().toString();
        // if (IGNORED_URIS.contains(requestURI) || requestURI.startsWith("/admin/")
        // || requestURI.startsWith("/auth") || requestURI.startsWith("/api/products")
        // || requestURI
        // .startsWith("/api/category")) {
        // // Nếu là URI mà bạn muốn bỏ qua và không kiểm tra JWT token, cho phép tiếp
        // tục
        // // xử lý request
        // filterChain.doFilter(request, response);
        // return;
        // }

        // if (requestURL.contains(".")) {
        // // Cho phép yêu cầu này không cần token và tiếp tục xử lý các yêu cầu khác
        // bình
        // // thường
        // filterChain.doFilter(request, response);
        // return;
        // }

        // if (!containsJwtToken(request)) {
        // // Nếu không có JWT token, trả về lỗi 401 (UNAUTHORIZED)
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // response.getWriter().write("Unauthorized: JWT token is missing");
        // return;
        // }
        // final String header = request.getHeader("Authorization");
        // if (header != null && header.startsWith("Bearer ")) {
        // if (requestURI.startsWith("/api/getNewToken")) {
        // filterChain.doFilter(request, response);
        // return;
        // }
        if (extractJwtToken(request) != null) {
            try {
                String jwtToken = extractJwtToken(request);
                System.out.println(jwtToken.toString());

                if (jwtTokenUtil.isTokenExpired(jwtToken)) {
                    ResponseEntity<String> expiredResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Token has expired");
                    response.setContentType("application/json");
                    response.getWriter().write(expiredResponse.getBody());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                } else if (jwtTokenUtil.validateToken(jwtToken)) {
                    // Xác thực token và lấy thông tin người dùng từ token
                    String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                    UserDetails userDetails = userLoginService.loadUserByUsername(username);

                    // Tạo đối tượng Authentication từ UserDetails
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // Đặt đối tượng Authentication vào SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException ex) {
                // Xử lý token hết hạn
                jwtTokenUtil.handleResponseTokenError(response, HttpStatus.UNAUTHORIZED,
                        "Unauthorized: Token has expired");
                return;
            } catch (JwtException ex) {
                // Xử lý nếu token không hợp lệ
                jwtTokenUtil.handleResponseTokenError(response, HttpStatus.UNAUTHORIZED,
                        "Unauthorized: Invalid token");
                return;
            }
        }
        // }

        // Tiếp tục xử lý yêu cầu
        filterChain.doFilter(request, response);
    }

    // Phương thức hỗ trợ để trích xuất JWT token từ header Authorization
    private String extractJwtToken(HttpServletRequest request) {
        String headerValue = request.getHeader("Authorization");
        if (headerValue != null && headerValue.startsWith("Bearer ")) {
            return headerValue.substring(7);
        }
        return null;
    }

    // private boolean containsJwtToken(HttpServletRequest request) {
    // String jwtToken = extractJwtToken(request);
    // return jwtToken != null;
    // }

}

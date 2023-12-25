package com.poly.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.bean.User;
import com.poly.service.AuthorityService;
import com.poly.service.CookieService;
import com.poly.service.MailService;
import com.poly.service.RoleService;
import com.poly.service.UserLoginService;
import com.poly.service.UserService;
import com.poly.util.JWTTokenUtil;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AuthRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    MailService mailService;

    @Autowired
    JWTTokenUtil jwtTokenUtil;

    @Autowired
    HttpSession session;

    @Autowired
    UserService userService;

    @Autowired
    CookieService cookieService;

    @Autowired
    RoleService roleService;

    @Autowired
    AuthorityService authorityService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User userLogin) {
        System.out.println("Request Data: " + userLogin);
        if (userService.existsById(userLogin.getId())) {
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
                User user = userService.findById(jwtTokenUtil.getUsernameFromToken(jwtToken));

                // Trả về phản hồi thành công với JWT token trong header "Authorization"
                Map<String, Object> response = new HashMap<>();
                response.put("name", user.getFullname());

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        // Xóa thông tin đăng nhập khỏi HttpSession
        session.invalidate();

        // Trả về phản hồi thành công
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng xuất thành công");
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/logout/success")
    public ResponseEntity<?> logout2() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng xuất thành công");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/authorization")
    public ResponseEntity<?> authentication(@RequestHeader("Authorization") Optional<String> authorizationHeader) {
        // Kiểm tra xem header "Authorization" có giá trị không
        if (authorizationHeader == null || !authorizationHeader.get().startsWith("Bearer ")) {
            // Nếu không có token hoặc không bắt đầu bằng "Bearer ", trả về lỗi 401
            // (UNAUTHORIZED)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Lấy token từ header
        String token = authorizationHeader.get().replace("Bearer ", "");

        // Xử lý token bằng JWTUtil để lấy danh sách roles
        List<String> roles = jwtTokenUtil.getRolesFromToken(token);

        // Xử lý và kiểm tra thông tin từ token
        // ... (các xử lý tùy ý)

        // Trả về thông tin
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login check");
        response.put("roles", roles);
        // ...
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userR) {
        System.out.println("User Register: " + userR);
        Map<String, Object> response = new HashMap<>();
        if (userService.existsById(userR.getId())) {
            response.put("message", "Đăng ký thất bại: Tên đăng nhập tồn tại");
            return ResponseEntity.status(400).body(response);
        } else if (userService.existsByEmail(userR.getEmail())) {
            response.put("message", "Đăng ký thất bại: Email tồn tại");
            return ResponseEntity.status(400).body(response);
        } else if (userService.existsByPhone(userR.getPhone())) {
            response.put("message", "Đăng ký thất bại: Số điện thoại tồn tại");
            return ResponseEntity.status(400).body(response);
        }

        User user = new User();
        user.setId(userR.getId());
        user.setPassword(userR.getPassword());
        user.setFullname(userR.getFullname());
        user.setEmail(userR.getEmail());
        user.setGender(userR.getGender());
        user.setBirthday(userR.getBirthday());
        user.setActive(true);
        user.setAddress(userR.getAddress());
        user.setPhone(userR.getPhone());
        userService.create(user);

        // Set role user
        authorityService.createUser(user);

        response.put("message", "Đăng ký thành công");
        // Gửi mail chào mừng
        mailService.sendMailWelcome(userR.getEmail(), userR.getFullname());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-user-info")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // Nếu không có token hoặc không bắt đầu bằng "Bearer ", trả về lỗi 401
            // (UNAUTHORIZED)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Lấy token từ header
        String token = authorizationHeader.replace("Bearer ", "");

        // Lây username từ token
        String username = jwtTokenUtil.getUsernameFromToken(token);
        System.out.println(username);

        User user = userService.findById(username);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/change-info")
    public ResponseEntity<?> change(@RequestBody User userN,
            @RequestHeader("Authorization") String authorizationHeader) {
        // Lấy token từ header
        String token = authorizationHeader.replace("Bearer ", "");

        // Lây username từ token
        String username = jwtTokenUtil.getUsernameFromToken(token);
        System.out.println(username);

        User userOld = userService.findById(username);
        System.out.println("User New: " + userN);
        Map<String, Object> response = new HashMap<>();
        if (userService.existsById(userN.getId()) && !userN.getId().equals(userOld.getId())) {
            response.put("message", "Đăng ký thất bại: Tên đăng nhập tồn tại");
            return ResponseEntity.status(400).body(response);
        } else if (userService.existsByEmail(userN.getEmail()) && !userN.getEmail().equals(userOld.getEmail())) {
            response.put("message", "Đăng ký thất bại: Email tồn tại");
            return ResponseEntity.status(400).body(response);
        } else if (userService.existsByPhone(userN.getPhone()) && !userN.getPhone().equals(userOld.getPhone())) {
            response.put("message", "Đăng ký thất bại: Số điện thoại tồn tại");
            return ResponseEntity.status(400).body(response);
        }

        User userChange = new User();
        userChange.setId(userN.getId());
        userChange.setPassword(userN.getPassword());
        userChange.setFullname(userN.getFullname());
        userChange.setEmail(userN.getEmail());
        userChange.setGender(userN.getGender());
        userChange.setBirthday(userN.getBirthday());
        userChange.setActive(true);
        userChange.setAddress(userN.getAddress());
        userChange.setPhone(userN.getPhone());
        userService.create(userChange);
        mailService.sendMailChangeInfo(userOld.getEmail(), userOld.getFullname());
        response.put("message", "Thay đổi thông tin thành công");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/get-otp")
    public ResponseEntity<?> getOTP(@RequestBody User forgotData) {
        Map<String, Object> response = new HashMap<>();
        if (!userService.existsById(forgotData.getId())) {
            response.put("message", "Tên đăng nhập không tồn tại");
            return ResponseEntity.badRequest().body(response);
        } else if (!userService.existsByEmail(forgotData.getEmail())
                || !forgotData.getEmail().equals(userService.findById(forgotData.getId()).getEmail())) {
            response.put("message", "Email không hợp lệ");
            return ResponseEntity.badRequest().body(response);
        }
        String otp = mailService.generateOTP(6);
        System.out.println("OTP " + otp);
        mailService.sendMailOTP(forgotData.getEmail(), otp);
        System.out.println("Forgot Data: " + forgotData);
        response.put("message", "Lấy OTP thành công");
        response.put("username", forgotData.getId());
        cookieService.addOTP("otp", otp, 1);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/check-otp")
    public ResponseEntity<?> checkOTP(@RequestBody Map<String, String> otp) {
        System.out.println("Client: " + otp.get("otp"));
        Map<String, Object> response = new HashMap<>();
        if (cookieService.get("otp") == null) {
            response.put("message", "OTP hết hạn");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            String otpC = cookieService.get("otp").getValue();
            System.out.println("server: " + otpC);
            if (!otp.get("otp").equals(otpC)) {
                response.put("message", "OTP chưa chính xác");
                return ResponseEntity.badRequest().body(response);
            } else {
                response.put("message", "Xác nhận OTP thành công");
                return ResponseEntity.ok().body(response);
            }

        }

    }

    @PostMapping("/change-pass")
    public ResponseEntity<?> change(@RequestBody Map<String, String> changeData) {
        Map<String, Object> response = new HashMap<>();
        if (changeData.isEmpty()) {
            response.put("message", "Không tìm thấy dữ liệu");
            return ResponseEntity.badRequest().body(response);
        }
        User user = userService.findById(changeData.get("username"));
        user.setPassword(changeData.get("password"));
        userService.create(user);
        response.put("message", "Đổi thành công mật khẩu");
        return ResponseEntity.ok().body(response);

    }
}

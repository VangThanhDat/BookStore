package com.poly.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.poly.repository.UserRepository;

@Service
public class UserLoginService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder pe;

    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            System.out.println("UserService......................");
            com.poly.bean.User user = userRepository.findById(username).orElse(null);
            System.out.println(user.getId() + " " + user.getPassword());
            String password = user.getPassword();
            List<String> listR = userRepository.findRoleIdsByUsername(username);
            for (String role : listR) {
                System.out.println("Role: " + role);
            }
            // String[] roles = { "ADMIN" };
            // String[] roles = account.getAuthorities().stream()
            // .map(au -> au.getRole().getId())
            // .collect(Collectors.toList()).toArray(new String[0]);
            // System.out.println("pass: " + password);
            // System.out.println("role: " + Arrays.toString(roles));
            return User.withUsername(username)
                    .password(password)
                    .roles(listR.toArray(new String[0])).build();
        } catch (Exception e) {
            throw new UsernameNotFoundException(username + "not found");
        }
    }

    public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) {
        System.out.println("OAuth2 User: " + oauth2.toString());

        String email = oauth2.getPrincipal().getAttribute("email");
        String fullname = oauth2.getPrincipal().getAttribute("name");
        String username = fullname.toLowerCase().replaceAll("\\s+", "");
        String password = pe.encode(username + email);

        String address = oauth2.getPrincipal().getAttribute("locale");

        if (!userService.existsByEmail(email)) {
            com.poly.bean.User user = new com.poly.bean.User();
            user.setId(username);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPhone("123456789");
            user.setActive(true);
            user.setGender(true);
            user.setAddress(address);
            user.setBirthday(new Date());
            user.setPassword(password);
            userService.create(user);
            System.out.println("Thêm vào database thành công");
            authorityService.createUser(user);

        }
        UserDetails user = User.withUsername(username)
                .password(password).roles("USER").build();

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // public com.poly.bean.User getAccountByUsername(String username) {
    // com.poly.bean.User user = userRepository.findById(username).get();
    // return user;
    // }
}

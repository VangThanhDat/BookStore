package com.poly.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.poly.filter.JwtTokenFilter;
import com.poly.service.UserLoginService;
import com.poly.util.JWTTokenUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        UserLoginService userLoginService;

        @Bean
        public JWTTokenUtil jwtTokenUtil() {
                return new JWTTokenUtil();
        }

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
                return super.authenticationManagerBean();
        }

        @Bean
        public JwtTokenFilter jwtTokenFilter() {
                return new JwtTokenFilter(this.jwtTokenUtil(), userLoginService);
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userLoginService);

        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                System.out.println("Security Check");
                http.cors();
                http.csrf().disable();

                http.authorizeRequests()
                                .antMatchers("/admin/index").hasRole("ADMIN")
                                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                                .antMatchers("/api/test", "/api/test/**").hasRole("ADMIN")
                                // Cấu hình các API không yêu cầu JWT token (public APIs)
                                .antMatchers("/api/login", "/auth/**", "/api/logout/**", "oauth2/login/**",
                                                "/api/vnpay/**", "/user/**", "api/products", "api/products/**",
                                                "api/category", "api/category/**", "/api/getNewToken",
                                                "/api/oauth2/user/login")
                                .permitAll()
                                // Cấu hình các API yêu cầu JWT token để truy cập
                                .antMatchers("/rest/**").authenticated()

                                // Cấu hình các API khác (có thể yêu cầu xác thực hoặc không)
                                .anyRequest().permitAll()
                                .and()
                                // Áp dụng JwtTokenFilter cho các API yêu cầu JWT token
                                .addFilterBefore(this.jwtTokenFilter(),
                                                UsernamePasswordAuthenticationFilter.class);

                http.formLogin()
                                .loginPage("/auth")
                                .loginProcessingUrl("/auth/login")
                                .defaultSuccessUrl("/auth/login/success", false)
                                .failureUrl("/auth/login/error")
                                .usernameParameter("username")
                                .passwordParameter("password");
                http.exceptionHandling()
                                .accessDeniedPage("/auth/access/denied");
                http.rememberMe()
                                .rememberMeParameter("rememberMe");
                http.logout()
                                .logoutUrl("/api/logout")
                                .logoutSuccessUrl("/api/logout/success");

                http.oauth2Login()
                                .loginPage("/auth/login/form")
                                .defaultSuccessUrl("/oauth2/login/success", true)
                                .failureUrl("/auth/login/error")
                                .authorizationEndpoint()
                                .baseUri("/oauth2/authorization");
                // http.addFilterBefore(new JwtTokenFilter(this.jwtTokenUtil(), userService),
                // UsernamePasswordAuthenticationFilter.class);

        }
}
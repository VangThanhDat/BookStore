package com.poly.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.poly.interceptor.LoggerInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    LoggerInterceptor logger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(logger)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/image/**", "/js/**", "/user/**", "/style/**", "/bootstrap4-2/**");
    }
}

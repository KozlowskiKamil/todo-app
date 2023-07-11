package io.github.mat3e.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    private Set<HandlerInterceptor> interceptor;

    public MvcConfiguration(Set<HandlerInterceptor> interceptor) {
        this.interceptor = interceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptor.forEach(registry::addInterceptor);
        registry.addInterceptor(new LoggerInterceptor());
    }
}

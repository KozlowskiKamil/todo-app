package io.github.mat3e.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class LoggerFilter implements Filter {

    public static final Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            var httpRequest = (HttpServletRequest) request;
            logger.info("[doFilter] " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());
        }
        chain.doFilter(request, response);
    }
}
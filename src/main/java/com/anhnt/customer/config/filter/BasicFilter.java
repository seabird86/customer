package com.anhnt.customer.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
@Order(1)
@Slf4j
public class BasicFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Inside Servlet Filter");
        log.info("User IP address: " + request.getLocalAddr());
        chain.doFilter(request, response);
        log.info("After Servlet Filter");
    }
}

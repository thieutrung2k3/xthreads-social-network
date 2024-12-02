package com.xthreads.auth_service.configuration;

import ch.qos.logback.core.util.StringUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CustomRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");

        if(StringUtils.hasText(authHeader)){
            requestTemplate.header("Authorization", authHeader);
        }
    }
}

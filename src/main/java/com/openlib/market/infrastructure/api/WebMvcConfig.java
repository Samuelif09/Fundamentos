package com.openlib.market.infrastructure.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiKeySecurityInterceptor apiKeySecurityInterceptor;

    public WebMvcConfig(ApiKeySecurityInterceptor apiKeySecurityInterceptor) {
        this.apiKeySecurityInterceptor = apiKeySecurityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeySecurityInterceptor);
    }
}

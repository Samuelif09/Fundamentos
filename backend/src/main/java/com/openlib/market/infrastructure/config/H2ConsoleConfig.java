package com.openlib.market.infrastructure.config;

import org.h2.server.web.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
public class H2ConsoleConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2ConsoleConfig.class);

    @Bean
    public ServletRegistrationBean<WebServlet> h2ConsoleServletRegistration() {
        ServletRegistrationBean<WebServlet> registrationBean =
                new ServletRegistrationBean<>(new WebServlet(), "/h2-console/*");
        registrationBean.setLoadOnStartup(1);
        LOGGER.info("[H2] Consola H2 registrada en /h2-console/*");
        return registrationBean;
    }
}

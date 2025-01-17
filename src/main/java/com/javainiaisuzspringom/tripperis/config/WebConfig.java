package com.javainiaisuzspringom.tripperis.config;

import com.javainiaisuzspringom.tripperis.utils.journal.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer  {
    @Autowired
    private LogInterceptor logInterceptor;

    @Value("${triperis.logging:false}")
    private String logging;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (logging != null && logging.trim().equals("true"))
            registry.addInterceptor(logInterceptor);
    }
}


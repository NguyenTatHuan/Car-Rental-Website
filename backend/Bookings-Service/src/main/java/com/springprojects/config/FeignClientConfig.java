package com.springprojects.config;

import com.springprojects.security.TokenContextHolder;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                String token = TokenContextHolder.getToken();
                if (token != null && !token.isBlank()) {
                    template.header("Authorization", "Bearer " + token);
                }
            }
        };
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(
                1, TimeUnit.MINUTES,
                60, TimeUnit.MINUTES,
                true
        );
    }

}

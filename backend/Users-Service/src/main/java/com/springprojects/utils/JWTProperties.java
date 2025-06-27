package com.springprojects.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JWTProperties {

    private String secret;

    private long expiration;

    private long refreshExpiration;

}

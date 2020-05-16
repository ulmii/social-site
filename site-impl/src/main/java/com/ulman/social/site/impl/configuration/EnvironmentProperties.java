package com.ulman.social.site.impl.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "social-site")
public class EnvironmentProperties
{
    private String apiVersion;
    private Security security;
    private TimeZone timeZone;

    @Getter
    @Setter
    public static class Security
    {
        private String secret;
        private long expirationTime;
        private String tokenPrefix;
        private String headerString;
        private String signUpUrl;
    }
}

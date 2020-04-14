package com.ulman.social.site.impl.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "social-site")
public class EnvironmentProperties
{
    private String apiVersion;
}

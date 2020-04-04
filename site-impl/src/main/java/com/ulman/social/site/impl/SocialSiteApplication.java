package com.ulman.social.site.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = { "com.ulman.social.site" })
@ImportResource(locations = { "classpath:beans.xml" })
public class SocialSiteApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SocialSiteApplication.class, args);
    }
}

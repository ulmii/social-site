package com.ulman.social.site.impl.domain.jersey.resourceConfig;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyResourceConfig extends ResourceConfig
{
    public JerseyResourceConfig()
    {
        packages("com.ulman.social.site");
    }
}

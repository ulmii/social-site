package com.ulman.social.site.impl.domain.jersey.resourceConfig;

import com.ulman.social.site.api.UserResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api")
public class JerseyResourceConfig extends ResourceConfig
{
    public JerseyResourceConfig()
    {
        packages("com.ulman.social.site");
        register(UserResource.class);
    }
}

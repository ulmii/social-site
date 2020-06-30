package com.ulman.social.site.impl.domain.jersey.resourceConfig;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

@Slf4j
@Configuration
public class JerseyResourceConfig extends ResourceConfig
{
    public JerseyResourceConfig()
    {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter(new AnnotationTypeFilter(Path.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
        provider.findCandidateComponents("com.ulman.social.site").forEach(beanDefinition -> {
            try
            {
                log.info("registering {} to jersey config", beanDefinition.getBeanClassName());
                register(Class.forName(beanDefinition.getBeanClassName()));
            }
            catch (ClassNotFoundException e)
            {
                log.warn("Failed to register: {}", beanDefinition.getBeanClassName());
            }
        });
    }
}

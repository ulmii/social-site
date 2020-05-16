package com.ulman.social.site.impl.domain.handler;

import com.ulman.social.site.impl.configuration.EnvironmentProperties;
import com.ulman.social.site.impl.domain.error.ApiError;
import com.ulman.social.site.impl.domain.error.AppError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class ApiErrorExceptionHandler implements ExceptionMapper<ApiError>
{
    private EnvironmentProperties environmentProperties;

    @Autowired
    public ApiErrorExceptionHandler(EnvironmentProperties environmentProperties)
    {
        this.environmentProperties = environmentProperties;
    }

    @Override
    public Response toResponse(ApiError e)
    {

        final AppError error = new AppError(
                environmentProperties.getTimeZone().toZoneId(),
                environmentProperties.getApiVersion(),
                e.getStatusType(),
                e.getMessage(),
                e.getDomain(),
                e.getClass().getSimpleName(),
                e.getErrorMessage()
        );

        return Response.status(e.getStatusType())
                .entity(error)
                .build();
    }
}

package com.ulman.social.site.impl.domain.error.exception.authentication;

import com.ulman.social.site.impl.domain.error.AppError;

import javax.ws.rs.core.Response;
import java.time.ZoneId;

public class AuthorizationException extends AppError
{
    public AuthorizationException(ZoneId timeZone, String apiVersion, Throwable exception)
    {
        super(timeZone, apiVersion, Response.Status.FORBIDDEN, exception.getMessage(), "Authorization", exception.toString(), exception.getMessage());
    }
}

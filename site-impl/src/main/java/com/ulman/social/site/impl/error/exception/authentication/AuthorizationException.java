package com.ulman.social.site.impl.error.exception.authentication;

import com.ulman.social.site.impl.error.exception.AppError;

import javax.ws.rs.core.Response;

public class AuthorizationException extends AppError
{
    public AuthorizationException(String apiVersion, Throwable exception)
    {
        super(apiVersion, Response.Status.FORBIDDEN, exception.getMessage(), "Authorization", exception.toString(), exception.getMessage());
    }
}

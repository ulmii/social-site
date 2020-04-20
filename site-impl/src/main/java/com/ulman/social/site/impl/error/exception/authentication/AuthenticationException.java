package com.ulman.social.site.impl.error.exception.authentication;

import com.ulman.social.site.impl.error.exception.ApiError;

import javax.ws.rs.core.Response;

public class AuthenticationException extends ApiError
{
    public AuthenticationException(String message)
    {
        super(Response.Status.FORBIDDEN, "Authentication", "Authentication failed", message);
    }
}

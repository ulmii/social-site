package com.ulman.social.site.impl.domain.error.exception.authentication;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class AuthenticationException extends ApiError
{
    public AuthenticationException(String message)
    {
        super(Response.Status.FORBIDDEN, "Authentication", "Authentication failed", message);
    }
}

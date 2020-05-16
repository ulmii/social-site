package com.ulman.social.site.impl.domain.error.exception;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class InvalidTokenException extends ApiError
{
    public InvalidTokenException(String message)
    {
        super(Response.Status.INTERNAL_SERVER_ERROR, "Authorization", "Invalid token", message);
    }
}

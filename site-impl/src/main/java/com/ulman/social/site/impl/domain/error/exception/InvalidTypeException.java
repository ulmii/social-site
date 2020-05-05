package com.ulman.social.site.impl.domain.error.exception;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class InvalidTypeException extends ApiError
{
    public InvalidTypeException(String message)
    {
        super(Response.Status.BAD_REQUEST, "Request", "This action can't be executed for given type", message);
    }
}

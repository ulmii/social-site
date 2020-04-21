package com.ulman.social.site.impl.domain.error.exception.user;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class UserAlreadyExistsException extends ApiError
{
    public UserAlreadyExistsException(String message)
    {
        super(Response.Status.BAD_REQUEST, "User", "User already exists", message);
    }
}

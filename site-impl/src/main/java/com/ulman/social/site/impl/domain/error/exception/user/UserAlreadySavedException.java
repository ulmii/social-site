package com.ulman.social.site.impl.domain.error.exception.user;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class UserAlreadySavedException extends ApiError
{
    public UserAlreadySavedException(String message)
    {
        super(Response.Status.BAD_REQUEST, "User", "User already saved", message);
    }
}

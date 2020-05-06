package com.ulman.social.site.impl.domain.error.exception.user;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class UserAlreadyHiddenException extends ApiError
{
    public UserAlreadyHiddenException(String message)
    {
        super(Response.Status.BAD_REQUEST, "User", "User already hidden", message);
    }
}

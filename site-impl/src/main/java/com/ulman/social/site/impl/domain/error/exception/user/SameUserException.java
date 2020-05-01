package com.ulman.social.site.impl.domain.error.exception.user;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class SameUserException extends ApiError
{
    public SameUserException(String message)
    {
        super(Response.Status.BAD_REQUEST, "User", "This action can't be executed for the same user", message);
    }
}

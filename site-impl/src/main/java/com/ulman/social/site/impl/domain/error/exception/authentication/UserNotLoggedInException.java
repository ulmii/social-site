package com.ulman.social.site.impl.domain.error.exception.authentication;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class UserNotLoggedInException extends ApiError
{
    public UserNotLoggedInException(String message)
    {
        super(Response.Status.FORBIDDEN, "User", "User not logged in", message);
    }
}

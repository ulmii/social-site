package com.ulman.social.site.impl.domain.error.exception.user;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class UserNotFollowedException extends ApiError
{
    public UserNotFollowedException(String message)
    {
        super(Response.Status.NOT_FOUND, "User", "User not followed", message);
    }
}

package com.ulman.social.site.impl.domain.error.exception.user;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class UserProfileNotPrivateException extends ApiError
{
    public UserProfileNotPrivateException(String message)
    {
        super(Response.Status.BAD_REQUEST, "User", "This action can't be executed for user with public profile", message);
    }
}

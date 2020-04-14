package com.ulman.social.site.impl.error.exception.user;

import com.ulman.social.site.impl.error.exception.ApiError;

import javax.ws.rs.core.Response;

public class UserDoesntExistException extends ApiError
{
    public UserDoesntExistException(String message)
    {
        super(Response.Status.NOT_FOUND, "User", "User does not exist", message);
    }
}

package com.ulman.social.site.impl.error.exception;

import javax.ws.rs.core.Response;

public class UserAlreadyExistsException extends ApiError
{
    public UserAlreadyExistsException(String message)
    {
        super(Response.Status.BAD_REQUEST, "User", "User already exists", message);
    }
}

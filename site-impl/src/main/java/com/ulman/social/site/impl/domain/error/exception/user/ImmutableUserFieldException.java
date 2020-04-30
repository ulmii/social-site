package com.ulman.social.site.impl.domain.error.exception.user;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class ImmutableUserFieldException extends ApiError
{
    public ImmutableUserFieldException(String message)
    {
        super(Response.Status.BAD_REQUEST, "User", "Can't change user field", message);
    }
}

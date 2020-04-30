package com.ulman.social.site.impl.domain.error.exception.post;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class ImmutablePostFieldException extends ApiError
{
    public ImmutablePostFieldException(String message)
    {
        super(Response.Status.BAD_REQUEST, "Post", "Can't change post field", message);
    }
}

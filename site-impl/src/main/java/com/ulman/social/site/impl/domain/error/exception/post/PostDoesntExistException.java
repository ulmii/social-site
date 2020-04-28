package com.ulman.social.site.impl.domain.error.exception.post;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class PostDoesntExistException extends ApiError
{
    public PostDoesntExistException(String message)
    {
        super(Response.Status.NOT_FOUND, "Post", "Post does not exist", message);
    }
}

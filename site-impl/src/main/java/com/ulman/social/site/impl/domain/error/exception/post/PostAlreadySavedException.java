package com.ulman.social.site.impl.domain.error.exception.post;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class PostAlreadySavedException extends ApiError
{
    public PostAlreadySavedException(String message)
    {
        super(Response.Status.BAD_REQUEST, "Post", "Post already saved", message);
    }
}

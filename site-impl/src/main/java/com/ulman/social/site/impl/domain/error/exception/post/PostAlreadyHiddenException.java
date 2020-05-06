package com.ulman.social.site.impl.domain.error.exception.post;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class PostAlreadyHiddenException extends ApiError
{
    public PostAlreadyHiddenException(String message)
    {
        super(Response.Status.BAD_REQUEST, "Post", "Post already hidden", message);
    }
}

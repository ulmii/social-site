package com.ulman.social.site.impl.domain.error.exception.comment;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class CommentDoesntExistException extends ApiError
{
    public CommentDoesntExistException(String message)
    {
        super(Response.Status.NOT_FOUND, "Comment", "Comment does not exist", message);
    }
}

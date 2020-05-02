package com.ulman.social.site.impl.domain.error.exception.comment;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class ImmutableCommentFieldException extends ApiError
{
    public ImmutableCommentFieldException(String message)
    {
        super(Response.Status.BAD_REQUEST, "Comment", "Can't change comment field", message);
    }
}

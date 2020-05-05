package com.ulman.social.site.impl.domain.error.exception.user;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class PendingFollowException extends ApiError
{
    public PendingFollowException(String message)
    {
        super(Response.Status.ACCEPTED, "User", "Follow pending", message);
    }
}

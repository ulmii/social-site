package com.ulman.social.site.impl.domain.error.exception.authentication;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class PrivateProfileException extends ApiError
{
    public PrivateProfileException(String message)
    {
        super(Response.Status.FORBIDDEN, "Authorization", "Access to user posts is restricted", message);
    }
}

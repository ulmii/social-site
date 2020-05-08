package com.ulman.social.site.impl.domain.error.exception;

import com.ulman.social.site.impl.domain.error.ApiError;

import javax.ws.rs.core.Response;

public class PaginationException extends ApiError
{
    public PaginationException(String message)
    {
        super(Response.Status.BAD_REQUEST, "Pagination", "Wrong pagination arguments", message);
    }
}

package com.ulman.social.site.impl.domain.error;

import lombok.Getter;

import javax.ws.rs.core.Response;

@Getter
public abstract class ApiError extends RuntimeException
{
    private Response.StatusType statusType;
    private String domain;
    private String errorMessage;

    public ApiError(final Response.StatusType statusType, final String domain, final String errorMessage, final String message)
    {
        super(message);
        this.statusType = statusType;
        this.domain = domain;
        this.errorMessage = errorMessage;
    }
}

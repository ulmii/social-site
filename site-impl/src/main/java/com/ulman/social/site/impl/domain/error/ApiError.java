package com.ulman.social.site.impl.domain.error;

import javax.ws.rs.core.Response;

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

    public Response.StatusType getStatusType()
    {
        return statusType;
    }

    public String getDomain()
    {
        return domain;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}

package com.ulman.social.site.impl.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UserNotFoundException extends WebApplicationException
{
    public UserNotFoundException(String message)
    {
        super(Response.status(Response.Status.NOT_FOUND)
                .entity(message)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build());
    }
}

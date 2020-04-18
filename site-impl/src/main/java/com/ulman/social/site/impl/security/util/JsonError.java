package com.ulman.social.site.impl.security.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.core.Response;

@AllArgsConstructor
@Getter
@Setter
public class JsonError
{
    public Object object;
    public Response.StatusType statusType;
}

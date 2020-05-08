package com.ulman.social.site.impl.domain.error.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.core.Response;

@AllArgsConstructor
@Getter
@Setter
public class JsonResponse
{
    public Object object;
    public Response.StatusType statusType;
}

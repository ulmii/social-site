package com.ulman.social.site.api;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.LogoutService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("/logout")
public class LogoutResource
{
    private LogoutService logoutService;

    @Inject
    public LogoutResource(LogoutService logoutService)
    {
        this.logoutService = logoutService;
    }

    @GET
    public UserDto logout(@HeaderParam("Authorization") String token)
    {
        return logoutService.logout(token);
    }
}

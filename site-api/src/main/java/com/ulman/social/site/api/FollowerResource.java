package com.ulman.social.site.api;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.FollowerService;
import org.springframework.data.domain.Page;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/users/{userId}/followers")
public class FollowerResource
{
    private FollowerService followerService;

    @Inject
    public FollowerResource(FollowerService followerService)
    {
        this.followerService = followerService;
    }

    @GET
    public Page<UserDto> getFollowers(
            @PathParam("userId") String userId,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset)
    {
        return followerService.getFollowers(userId, limit, offset);
    }

    @GET
    @Path("/pending")
    public Page<UserDto> getPendingFollowers(
            @PathParam("userId") String userId,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset)
    {
        return followerService.getPendingFollowers(userId, limit, offset);
    }

    @PUT
    @Path("/{followerId}")
    public UserDto acceptPendingFollower(
            @PathParam("userId") String userId,
            @PathParam("followerId") String followerId)
    {
        return followerService.acceptPendingFollower(userId, followerId);
    }
}

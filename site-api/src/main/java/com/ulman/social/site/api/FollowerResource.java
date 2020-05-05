package com.ulman.social.site.api;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.FollowerService;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    public List<UserDto> getFollowers(
            @PathParam("userId") String userId)
    {
        return followerService.getFollowers(userId);
    }

    @GET
    @Path("/pending")
    public List<UserDto> getPendingFollowers(
            @PathParam("userId") String userId)
    {
        return followerService.getPendingFollowers(userId);
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

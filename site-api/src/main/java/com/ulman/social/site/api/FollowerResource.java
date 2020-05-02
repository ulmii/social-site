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
@Path("/users/{userId}")
public class FollowerResource
{
    private FollowerService followerService;

    @Inject
    public FollowerResource(FollowerService followerService)
    {
        this.followerService = followerService;
    }

    @GET
    @Path("/followers")
    public List<UserDto> getFollowers(
            @PathParam("userId") String userId)
    {
        return followerService.getFollowers(userId);
    }

    @GET
    @Path("/following")
    public List<UserDto> getFollowing(
            @PathParam("userId") String userId)
    {
        return followerService.getFollowing(userId);
    }

    @PUT
    @Path("/following/{userToFollowId}")
    public List<UserDto> addFollower(
            @PathParam("userId") String userId,
            @PathParam("userToFollowId") String userToFollowId)
    {
        return followerService.addFollower(userId, userToFollowId);
    }

    @DELETE
    @Path("/following/{userToUnfollowId}")
    public UserDto deleteFollower(
            @PathParam("userId") String userId,
            @PathParam("userToUnfollowId") String userToUnfollowId)
    {
        return followerService.deleteFollower(userId, userToUnfollowId);
    }
}

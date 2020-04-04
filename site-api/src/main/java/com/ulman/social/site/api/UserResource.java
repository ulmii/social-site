package com.ulman.social.site.api;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Named
@Produces(MediaType.APPLICATION_JSON)
@Path("/users")
public class UserResource
{
    private UserService userService;

    @Inject
    public UserResource(UserService userService)
    {
        this.userService = userService;
    }

    @GET
    public List<UserDto> getUsers()
    {
        return userService.getUsers();
    }

    @POST
    public Response addUser(UserDto userDto)
    {
        userService.addUser(userDto);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{userId}")
    public UserDto getUser(
            @PathParam("userId") String userId)
    {
        return userService.getUser();
    }

    @PUT
    @Path("{userId}")
    public Response updateUser(
            @PathParam("userId") String userId)
    {
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{userId}/followers")
    public UserDto getUserFollowers(
            @PathParam("userId") String userId)
    {
        return null;
    }

    @GET
    @Path("{userId}/following")
    public UserDto getUserFollowing(
            @PathParam("userId") String userId)
    {
        return null;
    }

    @PUT
    @Path("{userId}/following/{userToFollowId}")
    public Response addUserFollower(
            @PathParam("userId") String userId, @PathParam("userToFollowId") String userToFollowId)
    {
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{userId}/following/{userToUnfollowId}")
    public Response deleteUserFollower(
            @PathParam("userId") String userId, @PathParam("userToUnfollowId") String userToUnfollowId)
    {
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{userId}/hidden")
    public List<UserDto> getUserHidden(
            @PathParam("userId") String userId)
    {
        return null;
    }

    @PUT
    @Path("{userId}/hidden/{id}")
    public List<UserDto> updateUserHidden(
            @PathParam("userId") String userId, @PathParam("id") String id, @QueryParam("type") String type)
    {
        return null;
    }

    @GET
    @Path("{userId}/saved")
    public List<UserDto> getUserSaved(
            @PathParam("userId") String userId)
    {
        return null;
    }

    @PUT
    @Path("{userId}/saved/{id}")
    public List<UserDto> updateUserSaved(
            @PathParam("userId") String userId, @PathParam("id") String id)
    {
        return null;
    }

    @GET
    @Path("{userId}/posts")
    public List<UserDto> getUserPosts(
            @PathParam("userId") String userId)
    {
        return null;
    }

    @GET
    @Path("{userId}/comments")
    public List<UserDto> getUserComments(
            @PathParam("userId") String userId)
    {
        return null;
    }
}



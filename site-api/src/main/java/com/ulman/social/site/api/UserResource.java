package com.ulman.social.site.api;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;
import com.ulman.social.site.api.validation.OnCreate;
import com.ulman.social.site.api.validation.OnUpdate;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    public UserDto addUser(
            @NotNull @Valid @ConvertGroup(to = OnCreate.class) UserDto userDto)
    {
        return userService.addUser(userDto);
    }

    @GET
    @Path("/{userId}")
    public UserDto getUser(
            @PathParam("userId") String userId)
    {
        return userService.getUser(userId);
    }

    @PATCH
    @Path("/{userId}")
    public UserDto updateUser(
            @PathParam("userId") String userId,
            @NotNull @Valid @ConvertGroup(to = OnUpdate.class) UserDto userDto)
    {
        return userService.updateUser(userId, userDto);
    }

    @DELETE
    @Path("/{userId}")
    public UserDto deleteUser(
            @PathParam("userId") String userId)
    {
        return userService.deleteUser(userId);
    }

    @GET
    @Path("/{userId}/followers")
    public List<UserDto> getFollowers(
            @PathParam("userId") String userId)
    {
        return userService.getFollowers(userId);
    }

    @GET
    @Path("/{userId}/following")
    public List<UserDto> getFollowing(
            @PathParam("userId") String userId)
    {
        return userService.getFollowing(userId);
    }

    @PUT
    @Path("/{userId}/following/{userToFollowId}")
    public List<UserDto> addFollower(
            @PathParam("userId") String userId,
            @PathParam("userToFollowId") String userToFollowId)
    {
        return userService.addFollower(userId, userToFollowId);
    }

    @DELETE
    @Path("/{userId}/following/{userToUnfollowId}")
    public List<UserDto> deleteFollower(
            @PathParam("userId") String userId,
            @PathParam("userToUnfollowId") String userToUnfollowId)
    {
        return userService.deleteFollower(userId, userToUnfollowId);
    }

    @GET
    @Path("/{userId}/hidden")
    public List<UserDto> getHidden(
            @PathParam("userId") String userId)
    {
        return userService.getHidden(userId);
    }

    @PUT
    @Path("/{userId}/hidden/{id}")
    public List<UserDto> updateHidden(
            @PathParam("userId") String userId,
            @PathParam("id") String id,
            @QueryParam("type") String type)
    {
        return userService.updateHidden(userId, type);
    }

    @GET
    @Path("/{userId}/saved")
    public List<String> getSaved(
            @PathParam("userId") String userId)
    {
        return userService.getSaved(userId);
    }

    @PUT
    @Path("/{userId}/saved/{id}")
    public List<String> updateSaved(
            @PathParam("userId") String userId,
            @PathParam("id") String id)
    {
        return userService.updateSaved(userId, id);
    }

}



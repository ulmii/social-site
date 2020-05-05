package com.ulman.social.site.api;

import com.ulman.social.site.api.model.ContainerDto;
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
    @Path("/{userId}/hidden")
    public List<ContainerDto> getHidden(
            @PathParam("userId") String userId)
    {
        return userService.getHidden(userId);
    }

    @PUT
    @Path("/{userId}/hidden/{id}")
    public Object addHidden(
            @PathParam("userId") String userId,
            @PathParam("id") String id,
            @QueryParam("type") String type)
    {
        return userService.addHidden(userId, type);
    }

    @GET
    @Path("/{userId}/saved")
    public ContainerDto getSaved(
            @PathParam("userId") String userId)
    {
        return userService.getSaved(userId);
    }

    @PUT
    @Path("/{userId}/saved/{id}")
    public Object addSaved(
            @PathParam("userId") String userId,
            @PathParam("id") String id,
            @NotNull @QueryParam("type") String type)
    {
        return userService.addSaved(userId, id, type);
    }
}

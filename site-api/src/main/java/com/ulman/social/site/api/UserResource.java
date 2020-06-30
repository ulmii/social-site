package com.ulman.social.site.api;

import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;
import com.ulman.social.site.api.validation.OnCreate;
import com.ulman.social.site.api.validation.OnUpdate;
import org.springframework.data.domain.Page;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
    public Page<UserDto> getUsers(
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset)
    {
        return userService.getUsers(limit, offset);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(
            @Context UriInfo uriInfo,
            @NotNull @Valid @ConvertGroup(to = OnCreate.class) UserDto userDto)
    {
        UserDto user = userService.addUser(userDto);

        return Response.created(uriInfo.getBaseUriBuilder()
                .path(UserResource.class)
                .segment(user.getId())
                .build())
                .entity(user)
                .build();
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
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Path("/{userId}/comments")
    public Page<CommentDto> getUserComments(
            @PathParam("userId") String userId,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset)
    {
        return userService.getUserComments(userId, limit, offset);
    }

    @GET
    @Path("/{userId}/hidden/users")
    public Page<UserDto> getHiddenUsers(
            @PathParam("userId") String userId,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset)

    {
        return userService.getHiddenUsers(userId, limit, offset);
    }

    @GET
    @Path("/{userId}/hidden/posts")
    public Page<PostDto> getHiddenPosts(
            @PathParam("userId") String userId,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset)
    {
        return userService.getHiddenPosts(userId, limit, offset);
    }

    @PUT
    @Path("/{userId}/hidden/users/{id}")
    public UserDto addHiddenUser(
            @PathParam("userId") String userId,
            @PathParam("id") String id)
    {
        return userService.addHiddenUser(userId, id);
    }

    @PUT
    @Path("/{userId}/hidden/posts/{id}")
    public PostDto addHiddenPost(
            @PathParam("userId") String userId,
            @PathParam("id") String id)
    {
        return userService.addHiddenPost(userId, id);
    }

    @DELETE
    @Path("/{userId}/hidden/users/{id}")
    public UserDto removeHiddenUser(
            @PathParam("userId") String userId,
            @PathParam("id") String id)
    {
        return userService.removeHiddenUser(userId, id);
    }

    @DELETE
    @Path("/{userId}/hidden/posts/{id}")
    public PostDto removeHiddenPost(
            @PathParam("userId") String userId,
            @PathParam("id") String id)
    {
        return userService.removeHiddenPost(userId, id);
    }

    @GET
    @Path("/{userId}/saved/users")
    public Page<UserDto> getSavedUsers(
            @PathParam("userId") String userId,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset)
    {
        return userService.getSavedUsers(userId, limit, offset);
    }

    @GET
    @Path("/{userId}/saved/posts")
    public Page<PostDto> getSavedPosts(
            @PathParam("userId") String userId,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset)
    {
        return userService.getSavedPosts(userId, limit, offset);
    }

    @PUT
    @Path("/{userId}/saved/users/{id}")
    public UserDto addSavedUser(
            @PathParam("userId") String userId,
            @PathParam("id") String id)
    {
        return userService.addSavedUser(userId, id);
    }

    @PUT
    @Path("/{userId}/saved/posts/{id}")
    public PostDto addSaved(
            @PathParam("userId") String userId,
            @PathParam("id") String id)
    {
        return userService.addSavedPost(userId, id);
    }

    @DELETE
    @Path("/{userId}/saved/users/{id}")
    public UserDto removeSavedUser(
            @PathParam("userId") String userId,
            @PathParam("id") String id)
    {
        return userService.removeSavedUser(userId, id);
    }

    @DELETE
    @Path("/{userId}/saved/posts/{id}")
    public PostDto removeSavedPost(
            @PathParam("userId") String userId,
            @PathParam("id") String id)
    {
        return userService.removeSavedPost(userId, id);
    }
}

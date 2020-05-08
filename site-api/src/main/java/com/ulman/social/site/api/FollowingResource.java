package com.ulman.social.site.api;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.FollowerService;
import com.ulman.social.site.api.service.PostService;
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
@Path("/users/{userId}/following")
public class FollowingResource
{
    private FollowerService followerService;
    private PostService postService;

    @Inject
    public FollowingResource(FollowerService followerService, PostService postService)
    {
        this.followerService = followerService;
        this.postService = postService;
    }

    @GET
    public List<UserDto> getFollowing(
            @PathParam("userId") String userId)
    {
        return followerService.getFollowing(userId);
    }

    @PUT
    @Path("/{userToFollowId}")
    public List<UserDto> addFollower(
            @PathParam("userId") String userId,
            @PathParam("userToFollowId") String userToFollowId)
    {
        return followerService.addFollower(userId, userToFollowId);
    }

    @DELETE
    @Path("/{userToUnfollowId}")
    public UserDto deleteFollower(
            @PathParam("userId") String userId,
            @PathParam("userToUnfollowId") String userToUnfollowId)
    {
        return followerService.deleteFollower(userId, userToUnfollowId);
    }

    @GET
    @Path("/posts")
    public Page<PostDto> getFollowingPosts(
            @PathParam("userId") String userId,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset
    )
    {
        return postService.getFollowingPosts(userId, limit, offset);
    }
}

package com.ulman.social.site.api;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.service.PostService;
import org.springframework.data.domain.Page;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Produces(MediaType.APPLICATION_JSON)
@Path("/users/{userId}/posts")
public class PostResource
{
    private PostService postService;

    @Inject
    public PostResource(PostService postService)
    {
        this.postService = postService;
    }

    @GET
    public Page<PostDto> getUserPosts(
            @PathParam("userId") String id,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset
    )
    {
        return postService.getUserPosts(id, limit, offset);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUserPost(
            @Context UriInfo uriInfo,
            @PathParam("userId") String id,
            @NotNull @Valid PostDto postDto
    )
    {
        PostDto post = postService.addUserPost(id, postDto);

        return Response.created(uriInfo.getBaseUriBuilder()
                .path(PostResource.class)
                .segment(post.getId())
                .build(post.getUserId()))
                .entity(post)
                .build();
    }

    @GET
    @Path("{postId}")
    public PostDto getPost(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId
    )
    {
        return postService.getPost(userId, postId);
    }

    @PATCH
    @Path("/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public PostDto updatePost(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId,
            @NotNull @Valid PostDto postDto
    )
    {
        return postService.updatePost(userId, postId, postDto);
    }

    @DELETE
    @Path("/{postId}")
    public PostDto deletePost(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId
    )
    {
        return postService.deletePost(userId, postId);

    }
}

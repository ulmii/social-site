package com.ulman.social.site.api;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.service.PostService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
    public List<PostDto> getUserPosts(
            @PathParam("userId") String id
    )
    {
        return postService.getUserPosts(id);
    }

    @POST
    public PostDto addUserPost(
            @PathParam("userId") String id, @Valid PostDto postDto
    )
    {
        return postService.addUserPost(id, postDto);
    }
}

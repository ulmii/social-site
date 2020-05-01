package com.ulman.social.site.api;

import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.api.service.CommentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/users/{userId}/posts/{postId}/comments")
public class CommentResource
{
    private CommentService commentService;

    @Inject
    public CommentResource(CommentService commentService)
    {
        this.commentService = commentService;
    }

    @GET
    public List<CommentDto> getComments(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId
    )
    {
        return commentService.getComments(userId, postId);
    }

    @POST
    public CommentDto addComment(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId,
            @NotNull @Valid CommentDto commentDto
    )
    {
        return commentService.addComment(userId, postId, commentDto);
    }
}

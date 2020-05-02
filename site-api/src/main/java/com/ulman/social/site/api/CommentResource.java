package com.ulman.social.site.api;

import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.api.service.CommentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
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

    @GET
    @Path("/{commentId}")
    public CommentDto getComment(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId,
            @PathParam("commentId") String commentId)
    {
        return commentService.getComment(userId, postId, commentId);
    }

    @PATCH
    @Path("/{commentId}")
    public CommentDto updateComment(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId,
            @PathParam("commentId") String commentId,
            @NotNull @Valid CommentDto commentDto)
    {
        return commentService.updateComment(userId, postId, commentId, commentDto);
    }

    @DELETE
    @Path("/{commentId}")
    public CommentDto deleteComment(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId,
            @PathParam("commentId") String commentId)
    {
        return commentService.deleteComment(userId, postId, commentId);
    }
}

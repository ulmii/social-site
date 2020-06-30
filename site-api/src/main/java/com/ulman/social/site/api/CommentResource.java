package com.ulman.social.site.api;

import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.api.service.CommentService;
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
import javax.ws.rs.core.MediaType;

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
    public Page<CommentDto> getComments(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId,
            @QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset
    )
    {
        return commentService.getComments(userId, postId, limit, offset);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public CommentDto addComment(
            @PathParam("userId") String userId,
            @PathParam("postId") String postId,
            @NotNull @Valid CommentDto commentDto
    )
    {
        // TODO: add 201 CREATED
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
    @Consumes(MediaType.APPLICATION_JSON)
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

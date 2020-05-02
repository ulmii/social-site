package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.CommentDto;

import java.util.List;

public interface CommentService
{
    List<CommentDto> getComments(String userId, String postId);

    CommentDto addComment(String userId, String postId, CommentDto commentDto);

    CommentDto getComment(String userId, String postId, String commentId);

    CommentDto updateComment(String userId, String postId, String commentId, CommentDto commentDto);

    CommentDto deleteComment(String userId, String postId, String commentId);
}

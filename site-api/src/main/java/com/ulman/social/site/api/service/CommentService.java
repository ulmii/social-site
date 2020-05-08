package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.CommentDto;
import org.springframework.data.domain.Page;

public interface CommentService
{
    Page<CommentDto> getComments(String userId, String postId, int limit, int offset);

    CommentDto addComment(String userId, String postId, CommentDto commentDto);

    CommentDto getComment(String userId, String postId, String commentId);

    CommentDto updateComment(String userId, String postId, String commentId, CommentDto commentDto);

    CommentDto deleteComment(String userId, String postId, String commentId);
}

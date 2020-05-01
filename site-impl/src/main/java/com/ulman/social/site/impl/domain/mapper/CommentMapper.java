package com.ulman.social.site.impl.domain.mapper;

import com.devskiller.friendly_id.FriendlyId;
import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.impl.domain.model.db.Comment;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper
{
    public CommentDto mapExternal(Comment comment)
    {
        return CommentDto.builder()
                .withId(FriendlyId.toFriendlyId(comment.getId()))
                .withPostId(FriendlyId.toFriendlyId(comment.getPost().getId()))
                .withUserId(comment.getUser().getId())
                .withCreated(comment.getCreated())
                .withContent(comment.getContent())
                .withRootLevel(comment.getRootLevel())
                .build();
    }

    public Comment mapInternal(CommentDto commentDto, Post post, User user)
    {
        return Comment.builder()
                .withId(commentDto.getId() == null ? null : FriendlyId.toUuid(commentDto.getId()))
                .withPost(post)
                .withUser(user)
                .withContent(commentDto.getContent())
                .build();
    }
}

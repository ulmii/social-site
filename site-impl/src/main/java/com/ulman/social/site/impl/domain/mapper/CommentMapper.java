package com.ulman.social.site.impl.domain.mapper;

import com.devskiller.friendly_id.FriendlyId;
import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.impl.domain.model.db.Comment;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
                .withUpdated(comment.getUpdated())
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

    public final Page<CommentDto> mapEntityPageIntoDtoPage(Pageable pageRequest, Page<Comment> source)
    {
        return mapEntityPageIntoDtoPage(pageRequest, source, (post) -> true);
    }

    public final Page<CommentDto> mapEntityPageIntoDtoPage(Pageable pageRequest, Page<Comment> source, Predicate<Comment> postPredicate)
    {
        List<CommentDto> posts = source.getContent().stream()
                .filter(postPredicate)
                .map(this::mapExternal)
                .collect(Collectors.toList());
        return new PageImpl<>(posts, pageRequest, source.getTotalElements());
    }
}

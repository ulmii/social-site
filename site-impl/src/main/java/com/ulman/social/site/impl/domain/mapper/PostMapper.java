package com.ulman.social.site.impl.domain.mapper;

import com.devskiller.friendly_id.FriendlyId;
import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.impl.domain.model.db.Post;

import javax.swing.*;
import java.util.UUID;

public class PostMapper
{
    public static PostDto mapExternal(Post post)
    {
        return PostDto.builder()
                .withId(FriendlyId.toFriendlyId(post.getId()))
                .withUserId(post.getUser().getId())
                .withCreated(post.getCreated())
                .withDescription(post.getDescription())
                .withPhotos(post.getPhotos())
                .build();
    }

    public static Post mapInternal(PostDto postDto)
    {
        return Post.builder()
                .withId(postDto.getId() == null ? null : FriendlyId.toUuid(postDto.getId()))
                .withDescription(postDto.getDescription())
                .withPhotos(postDto.getPhotos())
                .build();
    }

    public static UUID mapInternalPostId(String postId)
    {
        return FriendlyId.toUuid(postId);
    }
}

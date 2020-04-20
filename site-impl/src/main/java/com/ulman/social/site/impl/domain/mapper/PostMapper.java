package com.ulman.social.site.impl.domain.mapper;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.impl.model.db.Post;
import com.ulman.social.site.impl.model.db.User;

public class PostMapper
{
    public static PostDto mapExternal(Post post)
    {
        return PostDto.builder()
                .withId(post.getId())
                .withUserId(post.getUser().getId())
                .withDescription(post.getDescription())
                .build();
    }

    public static Post mapInternal(PostDto postDto)
    {
        return Post.builder()
                .withDescription(postDto.getDescription())
                .build();
    }
}

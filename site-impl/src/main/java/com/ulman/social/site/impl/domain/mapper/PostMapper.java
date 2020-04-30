package com.ulman.social.site.impl.domain.mapper;

import com.devskiller.friendly_id.FriendlyId;
import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.impl.domain.model.db.BlobWrapper;
import com.ulman.social.site.impl.domain.model.db.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PostMapper
{
    private ImageMapper imageMapper;

    @Autowired
    public PostMapper(ImageMapper imageMapper)
    {
        this.imageMapper = imageMapper;
    }

    public PostDto mapExternal(Post post)
    {
        return PostDto.builder()
                .withId(FriendlyId.toFriendlyId(post.getId()))
                .withUserId(post.getUser().getId())
                .withCreated(post.getCreated())
                .withDescription(post.getDescription())
                .withPhotos(post.getPhotos().stream()
                        .map(BlobWrapper::getBlob)
                        .map(imageMapper::blobToStringMapper)
                        .collect(Collectors.toList()))
                .build();
    }

    public Post mapInternal(PostDto postDto)
    {
        return Post.builder()
                .withId(postDto.getId() == null ? null : FriendlyId.toUuid(postDto.getId()))
                .withDescription(postDto.getDescription())
                .withPhotos(postDto.getPhotos().stream()
                        .map(imageMapper::stringToBlobMapper)
                        .map(BlobWrapper::new)
                        .collect(Collectors.toList()))
                .build();
    }

    public UUID mapInternalPostId(String postId)
    {
        return FriendlyId.toUuid(postId);
    }
}

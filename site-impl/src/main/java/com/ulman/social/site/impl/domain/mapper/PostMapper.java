package com.ulman.social.site.impl.domain.mapper;

import com.devskiller.friendly_id.FriendlyId;
import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.impl.domain.model.db.BlobWrapper;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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
                .withPhotos(post.getPhotos() == null ? null : mapBlobListToBase64List(post.getPhotos()))
                .build();
    }

    public Post mapInternal(PostDto postDto, User user)
    {
        return Post.builder()
                .withId(postDto.getId() == null ? null : FriendlyId.toUuid(postDto.getId()))
                .withUser(user)
                .withDescription(postDto.getDescription())
                .withPhotos(postDto.getPhotos() == null ? null : mapBase64ListToBlobList(postDto.getPhotos()))
                .build();
    }

    public List<BlobWrapper> mapBase64ListToBlobList(List<String> photos)
    {
        return photos.stream()
                .map(imageMapper::stringToBlobMapper)
                .map(BlobWrapper::new)
                .collect(Collectors.toList());
    }

    public List<String> mapBlobListToBase64List(List<BlobWrapper> photos)
    {
        return photos.stream()
                .map(BlobWrapper::getBlob)
                .map(imageMapper::blobToStringMapper)
                .collect(Collectors.toList());
    }

    public UUID mapInternalPostId(String postId)
    {
        return FriendlyId.toUuid(postId);
    }
}

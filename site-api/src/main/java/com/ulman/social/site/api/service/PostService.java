package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.PostDto;
import org.springframework.data.domain.Page;

public interface PostService
{
    Page<PostDto> getUserPosts(String id, int limit, int offset);

    Page<PostDto> getFollowingPosts(String id, int limit, int offset);

    PostDto addUserPost(String id, PostDto postDto);

    PostDto getPost(String userId, String postId);

    PostDto updatePost(String userId, String postId, PostDto postDto);

    PostDto deletePost(String userId, String postId);
}

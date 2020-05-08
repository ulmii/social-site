package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService
{
    Page<PostDto> getUserPosts(String id, int limit, int offset);

    List<PostDto> getFollowingPosts(String id);

    PostDto addUserPost(String id, PostDto postDto);

    PostDto getPost(String userId, String postId);

    PostDto updatePost(String userId, String postId, PostDto postDto);

    PostDto deletePost(String userId, String postId);
}

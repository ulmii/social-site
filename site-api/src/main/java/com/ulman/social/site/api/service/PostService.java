package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.PostDto;

import java.util.List;

public interface PostService
{
    List<PostDto> getUserPosts(String id);

    List<PostDto> getFollowingPosts(String id);

    PostDto addUserPost(String id, PostDto postDto);

    PostDto getPost(String userId, String postId);

    PostDto updatePost(String userId, String postId, PostDto postDto);

    PostDto deletePost(String userId, String postId);
}

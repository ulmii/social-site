package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.PostDto;

import java.util.List;

public interface PostService
{
    List<PostDto> getUserPosts(String id);

    PostDto addUserPost(String id, PostDto postDto);
}

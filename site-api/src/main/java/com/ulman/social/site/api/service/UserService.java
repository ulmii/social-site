package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.model.UserDto;
import org.springframework.data.domain.Page;

public interface UserService
{
    Page<UserDto> getUsers(int limit, int offset);

    UserDto addUser(UserDto userDto);

    UserDto getUser(String id);

    UserDto updateUser(String id, UserDto userDto);

    UserDto deleteUser(String userId);

    Page<CommentDto> getUserComments(String userId, int limit, int offset);

    Page<UserDto> getHiddenUsers(String userId, int limit, int offset);

    Page<PostDto> getHiddenPosts(String userId, int limit, int offset);

    UserDto addHiddenUser(String userId, String id);

    PostDto addHiddenPost(String userId, String id);

    UserDto removeHiddenUser(String userId, String id);

    PostDto removeHiddenPost(String userId, String id);

    Page<UserDto> getSavedUsers(String id, int limit, int offset);

    Page<PostDto> getSavedPosts(String id, int limit, int offset);

    UserDto addSavedUser(String userId, String id);

    PostDto addSavedPost(String userId, String id);

    UserDto removeSavedUser(String userId, String id);

    PostDto removeSavedPost(String userId, String id);
}

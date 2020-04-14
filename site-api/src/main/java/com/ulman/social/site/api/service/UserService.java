package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.UserDto;

import java.util.List;

public interface UserService
{
    List<UserDto> getUsers();

    UserDto addUser(UserDto userDto);

    UserDto getUser(String id);

    UserDto updateUser(String id);

    List<String> getFollowers(String id);

    List<String> getFollowing(String id);

    List<String> addFollower(String id, String id2);

    List<String> deleteFollower(String id, String id2);

    List<UserDto> getHidden(String id);

    List<UserDto> updateHidden(String id, String type);

    List<String> getSaved(String id);

    List<String> updateSaved(String id, String type);
}

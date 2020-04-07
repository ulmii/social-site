package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.UserDto;

import java.util.List;

public interface UserService
{
    List<UserDto> getUsers();
    void addUser(UserDto userDto);
    UserDto getUser(String id);
    void updateUser(UserDto userDto);
    List<UserDto> getFollowers(String id);
    List<UserDto> getFollowing(String id);
    void addFollower(String id, String id2);
}

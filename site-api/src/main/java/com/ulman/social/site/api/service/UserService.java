package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.ContainerDto;
import com.ulman.social.site.api.model.UserDto;

import java.util.List;

public interface UserService
{
    List<UserDto> getUsers();

    UserDto addUser(UserDto userDto);

    UserDto getUser(String id);

    UserDto updateUser(String id, UserDto userDto);

    List<ContainerDto> getHidden(String id);

    ContainerDto addHidden(String id, String type);

    ContainerDto getSaved(String id);

    ContainerDto addSaved(String userId, String id, String type);

    UserDto deleteUser(String userId);
}

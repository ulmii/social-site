package com.ulman.social.site.impl.domain.mapper;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.impl.model.db.User;

public class UserMapper
{
    public static UserDto mapInternal(User user)
    {
        return UserDto.builder()
                .withId(user.getId())
                .withName(user.getName())
                .withEmail(user.getEmail())
                .withPassword(user.getPassword())
                .withPhoto(user.getPhoto())
                .withDescription(user.getDescription())
                .withPublicProfile(user.getPublicProfile())
                .build();
    }

    public static UserDto maskSensitive(UserDto userDto)
    {
        userDto.setEmail(null);
        userDto.setPassword(null);

        return userDto;
    }
}

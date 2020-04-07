package com.ulman.social.site.impl.domain.mapper;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.impl.model.db.User;

public class UserMapper
{
    public static UserDto mapInternal(User user)
    {
        return UserDto.builder()
                .withId(user.getId())
                .withEmail(user.getEmail())
                .withPassword(user.getPassword())
                .build();
    }
}

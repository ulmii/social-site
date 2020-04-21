package com.ulman.social.site.impl.domain.mapper;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.impl.domain.model.db.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

public class UserMapper
{
    public static UserDto mapExternal(User user)
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

    public static User mapInternal(UserDto userDto, PasswordEncoder passwordEncoder)
    {
        return User.builder()
                .withId(userDto.getId())
                .withName(userDto.getName())
                .withEmail(userDto.getEmail())
                .withPassword(passwordEncoder.encode(userDto.getPassword()))
                .withPhoto(userDto.getPhoto())
                .withDescription(userDto.getDescription())
                .withPublicProfile(Objects.requireNonNullElse(userDto.getPublicProfile(), true))
                .build();
    }

    public static UserDto maskSensitive(UserDto userDto)
    {
        userDto.setEmail(null);
        userDto.setPassword(null);

        return userDto;
    }
}

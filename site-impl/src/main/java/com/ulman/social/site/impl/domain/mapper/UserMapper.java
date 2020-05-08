package com.ulman.social.site.impl.domain.mapper;

import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.impl.domain.model.db.Comment;
import com.ulman.social.site.impl.domain.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class UserMapper
{
    private ImageMapper imageMapper;

    @Autowired
    public UserMapper(ImageMapper imageMapper)
    {
        this.imageMapper = imageMapper;
    }

    public UserDto mapExternal(User user)
    {
        return UserDto.builder()
                .withId(user.getId())
                .withName(user.getName())
                .withEmail(user.getEmail())
                .withPassword(user.getPassword())
                .withPhoto(imageMapper.blobToStringMapper(user.getPhoto()))
                .withDescription(user.getDescription())
                .withPublicProfile(user.getPublicProfile())
                .withCreated(user.getCreated())
                .withUpdated(user.getUpdated())
                .build();
    }

    public User mapInternal(UserDto userDto, PasswordEncoder passwordEncoder)
    {
        return User.builder()
                .withId(userDto.getId())
                .withName(userDto.getName())
                .withEmail(userDto.getEmail())
                .withPassword(passwordEncoder.encode(userDto.getPassword()))
                .withPhoto(imageMapper.stringToBlobMapper(userDto.getPhoto()))
                .withDescription(userDto.getDescription())
                .withPublicProfile(Objects.requireNonNullElse(userDto.getPublicProfile(), true))
                .build();
    }

    public UserDto maskSensitive(UserDto userDto)
    {
        userDto.setEmail(null);
        userDto.setPassword(null);
        userDto.setUpdated(null);

        return userDto;
    }


    public final Page<UserDto> mapEntityPageIntoDtoPage(Pageable pageRequest, Page<User> source, boolean maskSensitive)
    {
        return mapEntityPageIntoDtoPage(pageRequest, source, maskSensitive, (user) -> true);
    }

    public final Page<UserDto> mapEntityPageIntoDtoPage(Pageable pageRequest, Page<User> source, boolean maskSensitive, Predicate<User> userPredicate)
    {
        List<UserDto> posts = source.getContent().stream()
                .filter(userPredicate)
                .map(this::mapExternal)
                .map(user -> maskSensitive ? maskSensitive(user) : user)
                .collect(Collectors.toList());
        return new PageImpl<>(posts, pageRequest, source.getTotalElements());
    }
}

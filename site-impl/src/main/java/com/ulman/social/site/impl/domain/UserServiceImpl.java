package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.model.db.User;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getUsers()
    {
        return userRepository.findAll().stream()
                .map(UserMapper::mapInternal)
                .collect(Collectors.toList());
    }

    @Override
    public void addUser(UserDto userDto)
    {
        if(accountExists(userDto.getId()))
        {
            throw new RuntimeException();
        }

        userRepository.save(User.builder()
                .withId(userDto.getId())
                .withEmail(userDto.getEmail())
                .withPublicProfile(userDto.isPublicProfile())
                .build());
    }

    @Override
    public UserDto getUser()
    {
        return null;
    }

    @Override
    public void updateUser(UserDto userDto)
    {

    }

    @Override
    public List<UserDto> getFollowers(String id)
    {
        return null;
    }

    @Override
    public List<UserDto> getFollowing(String id)
    {
        return null;
    }

    @Override
    public void addFollower(String id, String id2)
    {

    }

    private boolean accountExists(String id)
    {
        return userRepository.findById(id).isPresent();
    }
}

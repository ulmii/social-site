package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;
import com.ulman.social.site.impl.domain.error.exception.user.UserAlreadyExistsException;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.helper.UserHelper;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private UserHelper userHelper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getUsers()
    {
        return userRepository.findAll().stream()
                .map(userMapper::mapExternal)
                .map(userMapper::maskSensitive)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(UserDto userDto)
    {
        if (userHelper.accountExists(userDto.getId()))
        {
            throw new UserAlreadyExistsException(String.format("User with id: [%s] already exists", userDto.getId()));
        }
        else if (userHelper.emailExists(userDto.getEmail()))
        {
            throw new UserAlreadyExistsException(String.format("User with email: [%s] already exists", userDto.getEmail()));
        }

        return userMapper.mapExternal(userRepository.save(userMapper.mapInternal(userDto, passwordEncoder)));
    }

    @Override
    public UserDto getUser(String id)
    {
        User user = userHelper.getUserFromRepository(id);
        UserDto userDto = userMapper.mapExternal(user);

        if (userHelper.loggedUserIdMatchesWithRequest(id))
        {
            return userDto;
        }
        else
        {
            return userMapper.maskSensitive(userDto);
        }
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto)
    {
        User user = userHelper.authorizeAndGetUserById(id);

        return userMapper.mapExternal(userHelper.updateUserWithUserDto(user, userDto));

    }

    @Override
    public UserDto deleteUser(String userId)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can delete their account");

        userRepository.delete(user);

        return userMapper.mapExternal(user);
    }

    @Override
    public List<UserDto> getHidden(String id)
    {
        return null;
    }

    @Override
    public List<UserDto> updateHidden(String id, String type)
    {
        return null;
    }

    @Override
    public List<String> getSaved(String id)
    {
        return null;
    }

    @Override
    public List<String> updateSaved(String id, String type)
    {
        return null;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper)
    {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserHelper(UserHelper userHelper)
    {
        this.userHelper = userHelper;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder)
    {
        this.passwordEncoder = passwordEncoder;
    }
}

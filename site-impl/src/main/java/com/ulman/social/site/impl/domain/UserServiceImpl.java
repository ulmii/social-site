package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.error.exception.user.UserAlreadyExistsException;
import com.ulman.social.site.impl.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.model.db.User;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ulman.social.site.impl.domain.mapper.UserMapper.mapInternal;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> getUsers()
    {
        return userRepository.findAll().stream()
                .map(UserMapper::mapInternal)
                .map(UserMapper::maskSensitive)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(UserDto userDto)
    {
        if (accountExists(userDto.getId()))
        {
            throw new UserAlreadyExistsException(String.format("User with id: [%s] already exists", userDto.getId()));
        }
        else if (emailExists(userDto.getEmail()))
        {
            throw new UserAlreadyExistsException(String.format("User with email: [%s] already exists", userDto.getEmail()));
        }

        return mapInternal(userRepository.save(createUserFromUserDto(userDto)));
    }

    @Override
    public UserDto getUser(String id)
    {
        Optional<User> loggedUser = getLoggedUser();
        Optional<User> userFromRepository = userRepository.findById(id);

        if (userFromRepository.isPresent())
        {
            UserDto userDto = mapInternal(userFromRepository.get());
            if (loggedUser.isPresent() && loggedUser.get().getId().equals(id))
            {
                return userDto;
            }
            else
            {
                return UserMapper.maskSensitive(userDto);
            }
        }
        else
        {
            throw new UserDoesntExistException(String.format("User with id: [%s] does not exist", id));
        }
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto)
    {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent())
        {
            return mapInternal(updateUserWithUserDto(user.get(), userDto));
        }
        else
        {
            throw new UserDoesntExistException(String.format("User with id: [%s] does not exist", id));
        }
    }

    @Override
    public List<String> getFollowers(String id)
    {
        return null;
    }

    @Override
    public List<String> getFollowing(String id)
    {
        return null;
    }

    @Override
    public List<String> addFollower(String id, String id2)
    {
        return null;
    }

    @Override
    public List<String> deleteFollower(String id, String id2)
    {
        return null;
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

    private boolean accountExists(String id)
    {
        return userRepository.findById(id).isPresent();
    }

    private boolean emailExists(String email)
    {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean isFollowing(String id)
    {
        return true;
    }

    private Optional<User> getLoggedUser()
    {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal);
    }

    private User createUserFromUserDto(UserDto userDto)
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

    private User updateUserWithUserDto(User user, UserDto userDto)
    {
        userRepository.deleteById(user.getId());

        if (Objects.nonNull(userDto.getId()))
        {
            user.setId(user.getId());
        }

        if (Objects.nonNull(userDto.getPassword()))
        {
            user.setPassword(userDto.getPassword());
        }

        if (Objects.nonNull(userDto.getPublicProfile()))
        {
            user.setPublicProfile(userDto.getPublicProfile());
        }

        if (Objects.nonNull(userDto.getDescription()))
        {
            user.setDescription(userDto.getDescription());
        }

        if (Objects.nonNull(userDto.getEmail()))
        {
            user.setEmail(userDto.getEmail());
        }

        if (Objects.nonNull(userDto.getName()))
        {
            user.setName(userDto.getName());
        }

        if (Objects.nonNull(userDto.getPhoto()))
        {
            user.setPhoto(userDto.getPhoto());
        }

        return userRepository.save(user);
    }
}

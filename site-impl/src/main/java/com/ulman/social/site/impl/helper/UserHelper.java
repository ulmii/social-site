package com.ulman.social.site.impl.helper;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.impl.domain.error.exception.authentication.AuthenticationException;
import com.ulman.social.site.impl.domain.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.domain.mapper.ImageMapper;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class UserHelper
{
    private ImageMapper imageMapper;
    private UserRepository userRepository;

    @Autowired
    public UserHelper(ImageMapper imageMapper, UserRepository userRepository)
    {
        this.imageMapper = imageMapper;
        this.userRepository = userRepository;
    }

    public Optional<User> getLoggedUser()
    {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal);
    }

    public boolean loggedUserIdMatchesWithRequest(String id)
    {
        Optional<User> loggedUser = getLoggedUser();

        return loggedUser.isPresent() && loggedUser.get().getId().equals(id);
    }

    public boolean accountExists(String id)
    {
        return userRepository.findById(id).isPresent();
    }

    public boolean emailExists(String email)
    {
        return userRepository.findByEmail(email).isPresent();
    }

    public User getUserFromRepository(String userId)
    {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty())
        {
            throw new UserDoesntExistException(String.format("User with id: [%s] does not exist", userId));
        }

        return user.get();
    }

    public User authorizeAndGetUserById(String userId)
    {
        User user = getUserFromRepository(userId);

        if (!loggedUserIdMatchesWithRequest(userId))
        {
            throw new AuthenticationException("Only account owners can update their account");
        }

        return user;
    }

    public User updateUserWithUserDto(User user, UserDto userDto)
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
            user.setPhoto(imageMapper.stringToBlobMapper(userDto.getPhoto()));
        }

        return userRepository.save(user);
    }
}

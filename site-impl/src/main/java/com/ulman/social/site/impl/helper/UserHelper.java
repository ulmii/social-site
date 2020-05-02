package com.ulman.social.site.impl.helper;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.impl.domain.error.exception.authentication.AuthenticationException;
import com.ulman.social.site.impl.domain.error.exception.authentication.UserNotLoggedInException;
import com.ulman.social.site.impl.domain.error.exception.user.ImmutableUserFieldException;
import com.ulman.social.site.impl.domain.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.domain.mapper.ImageMapper;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
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

    public User getLoggedUser()
    {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal.matches("anonymousUser"))
        {
            throw new UserNotLoggedInException("You must be logged in to access this resource");
        }

        Optional<User> user = userRepository.findByEmail(principal);

        if(user.isEmpty())
        {
            throw new UserDoesntExistException("Logged in user doesn't exist in the repository", Response.Status.INTERNAL_SERVER_ERROR);
        }

        return user.get();
    }

    public boolean loggedUserIdMatchesWithRequest(String id)
    {
        User user = getLoggedUser();

        return user.getId().equals(id);
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
        return authorizeAndGetUserById(userId, "Only account owners can update their account");
    }

    public User authorizeAndGetUserById(String userId, String message)
    {
        User user = getUserFromRepository(userId);

        if (!loggedUserIdMatchesWithRequest(userId))
        {
            throw new AuthenticationException(message);
        }

        return user;
    }

    public User updateUserWithUserDto(User user, UserDto userDto)
    {
        if (Objects.nonNull(userDto.getId()))
        {
            throw new ImmutableUserFieldException("Field [id] in user can't be updated");
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

        return userRepository.saveAndFlush(user);
    }
}

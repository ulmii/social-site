package com.ulman.social.site.impl.helper;

import com.ulman.social.site.api.model.ContainerDto;
import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.impl.domain.error.exception.authentication.AuthenticationException;
import com.ulman.social.site.impl.domain.error.exception.authentication.UserNotLoggedInException;
import com.ulman.social.site.impl.domain.error.exception.user.ImmutableUserFieldException;
import com.ulman.social.site.impl.domain.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.domain.mapper.ImageMapper;
import com.ulman.social.site.impl.domain.mapper.PostMapper;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.Container;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserHelper
{
    private UserMapper userMapper;
    private PostMapper postMapper;
    private ImageMapper imageMapper;
    private UserRepository userRepository;

    @Autowired
    public UserHelper(UserMapper userMapper, PostMapper postMapper, ImageMapper imageMapper, UserRepository userRepository)
    {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
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

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public boolean isProfileNotAccessible(String userId)
    {
        User user = getUserFromRepository(userId);

        if (!user.getPublicProfile())
        {
            User loggedUser = getLoggedUser();
            return !user.getId().equals(loggedUser.getId()) && !user.getFollowers().contains(loggedUser);
        }

        return false;
    }

    public boolean accountExists(String id)
    {
        return userRepository.findById(id).isPresent();
    }

    public boolean emailExists(String email)
    {
        return userRepository.findByEmail(email).isPresent();
    }

    public Optional<User> getUserIfLoggedIn()
    {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal.matches("anonymousUser"))
        {
            return Optional.empty();
        }

        return userRepository.findByEmail(principal);
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

    public ContainerDto getSavedContainerFromUser(User user)
    {
        return mapContainerToContainerDto(user.getSaved());
    }

    public ContainerDto getHiddenContainerFromUser(User user)
    {
        return mapContainerToContainerDto(user.getHidden());
    }

    public ContainerDto mapContainerToContainerDto(Container container)
    {
        List<PostDto> posts = container
                .getPosts().stream()
                .map(postMapper::mapExternal)
                .collect(Collectors.toList());

        List<UserDto> users = container
                .getUsers().stream()
                .map(userMapper::mapExternal)
                .map(userMapper::maskSensitive)
                .collect(Collectors.toList());

        return new ContainerDto(posts, users);
    }
}

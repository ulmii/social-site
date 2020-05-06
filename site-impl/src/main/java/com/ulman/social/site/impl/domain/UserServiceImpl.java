package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.ContainerDto;
import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;
import com.ulman.social.site.impl.domain.error.exception.InvalidTypeException;
import com.ulman.social.site.impl.domain.error.exception.post.PostAlreadyHiddenException;
import com.ulman.social.site.impl.domain.error.exception.post.PostAlreadySavedException;
import com.ulman.social.site.impl.domain.error.exception.post.PostDoesntExistException;
import com.ulman.social.site.impl.domain.error.exception.user.SameUserException;
import com.ulman.social.site.impl.domain.error.exception.user.UserAlreadyExistsException;
import com.ulman.social.site.impl.domain.error.exception.user.UserAlreadyHiddenException;
import com.ulman.social.site.impl.domain.error.exception.user.UserAlreadySavedException;
import com.ulman.social.site.impl.domain.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.helper.PostHelper;
import com.ulman.social.site.impl.helper.UserHelper;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private UserHelper userHelper;
    private PostHelper postHelper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getUsers()
    {
        Stream<User> userStream = userRepository.findAll().stream();

        Optional<User> user = userHelper.getUserIfLoggedIn();
        if (user.isPresent())
        {
            List<User> hidden = user.get().getHidden().getUsers();
            userStream = userStream.filter(userToFilter -> !hidden.contains(userToFilter));
        }

        return userStream.map(userMapper::mapExternal)
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
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public ContainerDto getHidden(String id)
    {
        User user = userHelper.authorizeAndGetUserById(id, "Only account owners can manage their hidden collection");

        return userHelper.getHiddenContainerFromUser(user);
    }

    @Override
    @Transactional
    public ContainerDto addHidden(String userId, String id, String type)
    {
        User user = userHelper.authorizeAndGetUserById(userId);
        if (type.equals("user"))
        {
            User userToHide = userHelper.getUserFromRepository(id);
            if (user.equals(userToHide))
            {
                throw new SameUserException("Can't hide yourself");
            }

            if (user.getHidden().getUsers().contains(userToHide))
            {
                throw new UserAlreadyHiddenException("Can't hide same user twice");
            }

            user.getHidden().addUser(userToHide);
        }
        else if (type.equals("post"))
        {
            Post post = postHelper.getPostFromRepository(id);
            if (user.getHidden().getPosts().contains(post))
            {
                throw new PostAlreadyHiddenException("Can't hide same post twice");
            }

            user.getHidden().addPost(post);
        }
        else
        {
            throw new InvalidTypeException("Only [user|post] values are accepted as hide type ex. ?type=post");
        }

        return getHidden(userId);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public ContainerDto getSaved(String id)
    {
        User user = userHelper.authorizeAndGetUserById(id, "Only account owners can manage their saved collection");

        return userHelper.getSavedContainerFromUser(user);
    }

    @Override
    @Transactional
    public ContainerDto removeHidden(String userId, String id, String type)
    {
        User user = userHelper.authorizeAndGetUserById(userId);
        if (type.equals("user"))
        {
            User userToUnHide = userHelper.getUserFromRepository(id);
            if (user.equals(userToUnHide))
            {
                throw new SameUserException("Can't remove yourself from hidden");
            }

            if (!user.getHidden().getUsers().contains(userToUnHide))
            {
                throw new UserDoesntExistException("User not hidden");
            }

            user.getHidden().getUsers().remove(userToUnHide);
        }
        else if (type.equals("post"))
        {
            Post post = postHelper.getPostFromRepository(id);
            if (!user.getHidden().getPosts().contains(post))
            {
                throw new PostDoesntExistException("Post not hidden");
            }

            user.getHidden().getPosts().remove(post);
        }
        else
        {
            throw new InvalidTypeException("Only [user|post] values are accepted as save type ex. ?type=post");
        }

        return getHidden(userId);
    }

    @Override
    @Transactional
    public ContainerDto addSaved(String userId, String id, String type)
    {
        User user = userHelper.authorizeAndGetUserById(userId);
        if (type.equals("user"))
        {
            User userToSave = userHelper.getUserFromRepository(id);
            if (user.equals(userToSave))
            {
                throw new SameUserException("Can't save yourself");
            }

            if (user.getSaved().getUsers().contains(userToSave))
            {
                throw new UserAlreadySavedException("Can't save same user twice");
            }

            user.getSaved().addUser(userToSave);
        }
        else if (type.equals("post"))
        {
            Post post = postHelper.getPostFromRepository(id);
            if (user.getSaved().getPosts().contains(post))
            {
                throw new PostAlreadySavedException("Can't save same post twice");
            }

            user.getSaved().addPost(post);
        }
        else
        {
            throw new InvalidTypeException("Only [user|post] values are accepted as save type ex. ?type=post");
        }

        return getSaved(userId);
    }

    @Override
    @Transactional
    public ContainerDto removeSaved(String userId, String id, String type)
    {
        User user = userHelper.authorizeAndGetUserById(userId);
        if (type.equals("user"))
        {
            User userToRemove = userHelper.getUserFromRepository(id);
            if (user.equals(userToRemove))
            {
                throw new SameUserException("Can't remove yourself from saved");
            }

            if (!user.getSaved().getUsers().contains(userToRemove))
            {
                throw new UserDoesntExistException("User not saved");
            }

            user.getSaved().getUsers().remove(userToRemove);
        }
        else if (type.equals("post"))
        {
            Post post = postHelper.getPostFromRepository(id);
            if (!user.getSaved().getPosts().contains(post))
            {
                throw new PostDoesntExistException("Post not saved");
            }

            user.getSaved().getPosts().remove(post);
        }
        else
        {
            throw new InvalidTypeException("Only [user|post] values are accepted as save type ex. ?type=post");
        }

        return getSaved(userId);
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
    public void setPostHelper(PostHelper postHelper)
    {
        this.postHelper = postHelper;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder)
    {
        this.passwordEncoder = passwordEncoder;
    }
}

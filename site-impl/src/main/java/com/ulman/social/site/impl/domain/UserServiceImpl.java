package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;
import com.ulman.social.site.impl.domain.error.exception.post.PostAlreadyHiddenException;
import com.ulman.social.site.impl.domain.error.exception.post.PostAlreadySavedException;
import com.ulman.social.site.impl.domain.error.exception.post.PostDoesntExistException;
import com.ulman.social.site.impl.domain.error.exception.user.SameUserException;
import com.ulman.social.site.impl.domain.error.exception.user.UserAlreadyExistsException;
import com.ulman.social.site.impl.domain.error.exception.user.UserAlreadyHiddenException;
import com.ulman.social.site.impl.domain.error.exception.user.UserAlreadySavedException;
import com.ulman.social.site.impl.domain.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.domain.mapper.PostMapper;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.helper.PostHelper;
import com.ulman.social.site.impl.helper.UserHelper;
import com.ulman.social.site.impl.repository.OffsetPageRequest;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private PostMapper postMapper;
    private UserHelper userHelper;
    private PostHelper postHelper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserDto> getUsers(int limit, int offset)
    {
        OffsetPageRequest offsetPageRequest = OffsetPageRequest.of(limit, offset
                , Sort.by(Sort.Direction.ASC, "id"));

        Page<User> users = userRepository.findAll(offsetPageRequest);

        Predicate<User> hideUsers = (userToHide) -> true;
        Optional<User> user = userHelper.getUserIfLoggedIn();
        if (user.isPresent())
        {
            Set<User> hidden = user.get().getHiddenUsers();
            hideUsers = (userToFilter) -> !hidden.contains(userToFilter);
        }

        return userMapper.mapEntityPageIntoDtoPage(offsetPageRequest, users, true, hideUsers);
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
    @Transactional
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
    public Page<UserDto> getHiddenUsers(String userId, int limit, int offset)
    {
        userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their hidden collection");

        OffsetPageRequest offsetPageRequest = OffsetPageRequest.of(limit, offset);

        Page<User> users = userRepository.getHiddenUsers(userId, offsetPageRequest);

        return userMapper.mapEntityPageIntoDtoPage(offsetPageRequest, users, true);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Page<PostDto> getHiddenPosts(String userId, int limit, int offset)
    {
        userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their hidden collection");

        OffsetPageRequest offsetPageRequest = OffsetPageRequest.of(limit, offset);

        Page<Post> posts = userRepository.getHiddenPosts(userId, offsetPageRequest);

        return postMapper.mapEntityPageIntoDtoPage(offsetPageRequest, posts);
    }

    @Override
    @Transactional
    public UserDto addHiddenUser(String userId, String id)
    {
        User user = userHelper.authorizeAndGetUserById(userId);

        User userToHide = userHelper.getUserFromRepository(id);
        if (user.equals(userToHide))
        {
            throw new SameUserException("Can't hide yourself");
        }

        if (user.getHiddenUsers().contains(userToHide))
        {
            throw new UserAlreadyHiddenException("Can't hide same user twice");
        }

        user.getHiddenUsers().add(userToHide);

        return userMapper.maskSensitive(userMapper.mapExternal(userToHide));
    }

    @Override
    @Transactional
    public PostDto addHiddenPost(String userId, String id)
    {
        User user = userHelper.authorizeAndGetUserById(userId);
        Post post = postHelper.getPostFromRepository(id);

        if (user.getHiddenPosts().contains(post))
        {
            throw new PostAlreadyHiddenException("Can't hide same post twice");
        }

        user.getHiddenPosts().add(post);
        return postMapper.mapExternal(post);
    }

    @Override
    @Transactional
    public UserDto removeHiddenUser(String userId, String id)
    {
        User user = userHelper.authorizeAndGetUserById(userId);
        User userToUnHide = userHelper.getUserFromRepository(id);

        if (user.equals(userToUnHide))
        {
            throw new SameUserException("Can't remove yourself from hidden");
        }

        if (!user.getHiddenUsers().contains(userToUnHide))
        {
            throw new UserDoesntExistException("User not hidden");
        }

        user.getHiddenUsers().remove(userToUnHide);

        return userMapper.maskSensitive(userMapper.mapExternal(userToUnHide));
    }

    @Override
    @Transactional
    public PostDto removeHiddenPost(String userId, String id)
    {
        User user = userHelper.authorizeAndGetUserById(userId);

        Post post = postHelper.getPostFromRepository(id);
        if (!user.getHiddenPosts().contains(post))
        {
            throw new PostDoesntExistException("Post not hidden");
        }

        user.getHiddenPosts().remove(post);
        return postMapper.mapExternal(post);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Page<UserDto> getSavedUsers(String userId, int limit, int offset)
    {
        userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their saved collection");

        OffsetPageRequest offsetPageRequest = OffsetPageRequest.of(limit, offset);

        Page<User> users = userRepository.getSavedUsers(userId, offsetPageRequest);

        return userMapper.mapEntityPageIntoDtoPage(offsetPageRequest, users, true);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Page<PostDto> getSavedPosts(String userId, int limit, int offset)
    {
        userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their saved collection");

        OffsetPageRequest offsetPageRequest = OffsetPageRequest.of(limit, offset);

        Page<Post> posts = userRepository.getSavedPosts(userId, offsetPageRequest);

        return postMapper.mapEntityPageIntoDtoPage(offsetPageRequest, posts);
    }

    @Override
    @Transactional
    public UserDto addSavedUser(String userId, String id)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their saved collection");
        User userToSave = userHelper.getUserFromRepository(id);
        if (user.equals(userToSave))
        {
            throw new SameUserException("Can't save yourself");
        }

        if (user.getSavedUsers().contains(userToSave))
        {
            throw new UserAlreadySavedException("Can't save same user twice");
        }

        user.getSavedUsers().add(userToSave);

        return userMapper.maskSensitive(userMapper.mapExternal(userToSave));
    }

    @Override
    @Transactional
    public PostDto addSavedPost(String userId, String id)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their saved collection");

        Post post = postHelper.getPostFromRepository(id);
        if (user.getSavedPosts().contains(post))
        {
            throw new PostAlreadySavedException("Can't save same post twice");
        }

        user.getSavedPosts().add(post);

        return postMapper.mapExternal(post);
    }

    @Override
    @Transactional
    public UserDto removeSavedUser(String userId, String id)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their saved collection");

        User userToRemove = userHelper.getUserFromRepository(id);
        if (user.equals(userToRemove))
        {
            throw new SameUserException("Can't remove yourself from saved");
        }

        if (!user.getSavedUsers().contains(userToRemove))
        {
            throw new UserDoesntExistException("User not saved");
        }

        user.getSavedUsers().remove(userToRemove);

        return userMapper.maskSensitive(userMapper.mapExternal(userToRemove));
    }

    @Override
    @Transactional
    public PostDto removeSavedPost(String userId, String id)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their saved collection");
        Post post = postHelper.getPostFromRepository(id);
        if (!user.getSavedPosts().contains(post))
        {
            throw new PostDoesntExistException("Post not saved");
        }

        user.getSavedPosts().remove(post);

        return postMapper.mapExternal(post);
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
    public void setPostMapper(PostMapper postMapper)
    {
        this.postMapper = postMapper;
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

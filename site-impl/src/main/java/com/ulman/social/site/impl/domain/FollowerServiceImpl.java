package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.FollowerService;
import com.ulman.social.site.impl.domain.error.exception.user.PendingFollowException;
import com.ulman.social.site.impl.domain.error.exception.user.SameUserException;
import com.ulman.social.site.impl.domain.error.exception.user.UserAlreadyFollowedException;
import com.ulman.social.site.impl.domain.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.domain.error.exception.user.UserNotFollowedException;
import com.ulman.social.site.impl.domain.error.exception.user.UserProfileNotPrivateException;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.helper.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FollowerServiceImpl implements FollowerService
{
    private UserMapper userMapper;
    private UserHelper userHelper;

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<UserDto> getFollowers(String id)
    {
        User user = userHelper.getUserFromRepository(id);

        return user.getFollowers().stream()
                .map(userMapper::mapExternal)
                .map(userMapper::maskSensitive)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<UserDto> getFollowing(String id)
    {
        User user = userHelper.getUserFromRepository(id);

        return user.getFollowing().stream()
                .map(userMapper::mapExternal)
                .map(userMapper::maskSensitive)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(noRollbackFor = PendingFollowException.class)
    public List<UserDto> addFollower(String id, String id2)
    {
        User user = userHelper.authorizeAndGetUserById(id);
        User userToFollow = userHelper.getUserFromRepository(id2);

        if (user.equals(userToFollow))
        {
            throw new SameUserException("Can't follow yourself");
        }

        if (userToFollow.getFollowers().contains(user))
        {
            throw new UserAlreadyFollowedException("Can't follow the same user twice");
        }

        Set<User> users;
        if (userToFollow.getPublicProfile())
        {
            users = user.follow(userToFollow);
        }
        else
        {
            userToFollow.addPendingFollower(user);
            throw new PendingFollowException(String.format("You must wait till user [%s] accepts the follow request", userToFollow.getId()));
        }

        return users.stream()
                .map(userMapper::mapExternal)
                .map(userMapper::maskSensitive)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<UserDto> getPendingFollowers(String userId)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their follower requests");

        if(user.getPublicProfile())
        {
            throw new UserProfileNotPrivateException("Pending followers is only for users with profile set to private");
        }

        return user.getPendingFollowers().stream()
                .map(userMapper::mapExternal)
                .map(userMapper::maskSensitive)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto acceptPendingFollower(String userId, String followerId)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their follower requests");

        if(user.getPublicProfile())
        {
            throw new UserProfileNotPrivateException("Pending followers is only for users with profile set to private");
        }

        User userToAccept = userHelper.getUserFromRepository(followerId);

        if (user.equals(userToAccept))
        {
            throw new SameUserException("Can't follow yourself");
        }

        if (user.getFollowers().contains(user))
        {
            user.getPendingFollowers().remove(user);
            throw new UserAlreadyFollowedException("User already is accepted");
        }

        if(!user.getPendingFollowers().contains(userToAccept))
        {
            throw new UserDoesntExistException(String.format("User [%s] not found in pending followers", userToAccept.getId()));
        }

        user.acceptPendingFollower(userToAccept);

        return userMapper.maskSensitive(userMapper.mapExternal(userToAccept));
    }

    @Override
    @Transactional
    public UserDto deleteFollower(String id, String id2)
    {
        User user = userHelper.authorizeAndGetUserById(id);
        User userToUnfollow = userHelper.getUserFromRepository(id2);

        if (user.equals(userToUnfollow))
        {
            throw new SameUserException("Can't unfollow yourself");
        }

        if (!user.getFollowing().contains(userToUnfollow) || !userToUnfollow.getFollowers().contains(user))
        {
            throw new UserNotFollowedException(String.format("User with id [%s] is not following user with id [%s]", user.getId(), userToUnfollow.getId()));
        }

        return userMapper.maskSensitive(userMapper.mapExternal(user.unfollow(userToUnfollow)));
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
}

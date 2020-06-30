package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.FollowerService;
import com.ulman.social.site.impl.domain.error.exception.authentication.PrivateProfileException;
import com.ulman.social.site.impl.domain.error.exception.user.PendingFollowException;
import com.ulman.social.site.impl.domain.error.exception.user.SameUserException;
import com.ulman.social.site.impl.domain.error.exception.user.UserAlreadyFollowedException;
import com.ulman.social.site.impl.domain.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.domain.error.exception.user.UserNotFollowedException;
import com.ulman.social.site.impl.domain.error.exception.user.UserProfileNotPrivateException;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.helper.UserHelper;
import com.ulman.social.site.impl.repository.OffsetPageRequest;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class FollowerServiceImpl implements FollowerService
{
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserHelper userHelper;

    @Autowired
    public FollowerServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Page<UserDto> getFollowers(String id, int limit, int offset)
    {
        User user = userHelper.getUserFromRepository(id);

        if (userHelper.isProfileNotAccessible(id))
        {
            throw new PrivateProfileException(String.format("You must be one of [%s] followers to view followers", id));
        }

        OffsetPageRequest offsetPageRequest = OffsetPageRequest.of(limit, offset);

        Page<User> followers = userRepository.getFollowers(user.getId(), offsetPageRequest);
        return userMapper.mapEntityPageIntoDtoPage(offsetPageRequest, followers, true);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Page<UserDto> getFollowing(String id, int limit, int offset)
    {
        User user = userHelper.getUserFromRepository(id);

        if (userHelper.isProfileNotAccessible(id))
        {
            throw new PrivateProfileException(String.format("You must be one of [%s] followers to view following", id));
        }

        OffsetPageRequest offsetPageRequest = OffsetPageRequest.of(limit, offset,
                Sort.by(Sort.Direction.ASC, "id"));

        Page<User> following = userRepository.getFollowing(user.getId(), offsetPageRequest);
        return userMapper.mapEntityPageIntoDtoPage(offsetPageRequest, following, true);
    }

    @Override
    @Transactional(noRollbackFor = PendingFollowException.class)
    public UserDto addFollower(String id, String id2)
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

        if (userToFollow.getPublicProfile())
        {
            user.follow(userToFollow);
        }
        else
        {
            userToFollow.addPendingFollower(user);
            throw new PendingFollowException(String.format("You must wait till user [%s] accepts the follow request", userToFollow.getId()));
        }

        return userMapper.maskSensitive(userMapper.mapExternal(userToFollow));
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Page<UserDto> getPendingFollowers(String userId, int limit, int offset)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their follower requests");

        if (user.getPublicProfile())
        {
            throw new UserProfileNotPrivateException("Pending followers is only for users with profile set to private");
        }

        OffsetPageRequest offsetPageRequest = OffsetPageRequest.of(limit, offset,
                Sort.by(Sort.Direction.ASC, "id"));

        Page<User> pendingFollowers = userRepository.getPendingFollowers(user.getId(), offsetPageRequest);
        return userMapper.mapEntityPageIntoDtoPage(offsetPageRequest, pendingFollowers, true);
    }

    @Override
    @Transactional
    public UserDto acceptPendingFollower(String userId, String followerId)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can manage their follower requests");

        if (user.getPublicProfile())
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

        if (!user.getPendingFollowers().contains(userToAccept))
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

package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.UserDto;

import java.util.List;

public interface FollowerService
{
    List<UserDto> getFollowers(String id);

    List<UserDto> getFollowing(String id);

    List<UserDto> addFollower(String id, String id2);

    UserDto deleteFollower(String id, String id2);

    List<UserDto> getPendingFollowers(String userId);

    UserDto acceptPendingFollower(String userId, String followerId);
}

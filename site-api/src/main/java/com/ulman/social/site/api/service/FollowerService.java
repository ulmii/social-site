package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FollowerService
{
    Page<UserDto> getFollowers(String id, int limit, int offset);

    Page<UserDto> getFollowing(String id, int limit, int offset);

    List<UserDto> addFollower(String id, String id2);

    UserDto deleteFollower(String id, String id2);

    Page<UserDto> getPendingFollowers(String userId, int limit, int offset);

    UserDto acceptPendingFollower(String userId, String followerId);
}

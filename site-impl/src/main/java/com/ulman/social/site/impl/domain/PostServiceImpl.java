package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.service.PostService;
import com.ulman.social.site.impl.configuration.EnvironmentProperties;
import com.ulman.social.site.impl.domain.error.exception.authentication.PrivateProfileException;
import com.ulman.social.site.impl.domain.mapper.PostMapper;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.helper.PostHelper;
import com.ulman.social.site.impl.helper.UserHelper;
import com.ulman.social.site.impl.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService
{
    private PostRepository postRepository;
    private PostMapper postMapper;
    private UserHelper userHelper;
    private PostHelper postHelper;
    private EnvironmentProperties environmentProperties;

    @Autowired
    public PostServiceImpl(PostRepository postRepository)
    {
        this.postRepository = postRepository;
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<PostDto> getUserPosts(String userId)
    {
        if (userHelper.isProfileNotAccessible(userId))
        {
            throw new PrivateProfileException(String.format("You must be one of [%s] followers to view posts", userId));
        }

        List<Post> userPosts = postRepository.getPostsByUserId(userId);
        return userPosts.stream()
                .map(postMapper::mapExternal)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto addUserPost(String userId, PostDto postDto)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can add posts");

        Post post = postMapper.mapInternal(postDto, user);
        return postMapper.mapExternal(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostDto updatePost(String userId, String postId, PostDto postDto)
    {
        userHelper.authorizeAndGetUserById(userId, "Only account owners can update their posts");

        Post post = postHelper.getPostByUserIdAndPostId(userId, postId);

        return postMapper.mapExternal(postHelper.updatePostWithPostDto(post, postDto));
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public PostDto getPost(String userId, String postId)
    {
        if (userHelper.isProfileNotAccessible(userId))
        {
            throw new PrivateProfileException(String.format("You must be one of [%s] followers to view posts", userId));
        }

        Post post = postHelper.getPostByUserIdAndPostId(userId, postId);

        return postMapper.mapExternal(post);
    }

    @Override
    @Transactional
    public PostDto deletePost(String userId, String postId)
    {
        userHelper.authorizeAndGetUserById(userId, "Only account owners can delete their posts");

        Post post = postHelper.getPostByUserIdAndPostId(userId, postId);
        postRepository.delete(post);

        return postMapper.mapExternal(post);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<PostDto> getFollowingPosts(String userId)
    {
        User user = userHelper.authorizeAndGetUserById(userId, "Only account owners can see their following users post collection");

        Set<String> usersToFilter = user.getHidden()
                .getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        Set<UUID> postsToFilter = user.getHidden()
                .getPosts().stream()
                .map(Post::getId)
                .collect(Collectors.toSet());

        List<Post> userFollowingPosts = postRepository.getUserFollowingPosts(userId);

        return userFollowingPosts.stream()
                .filter(Predicate.not(post -> usersToFilter.contains(post.getUser().getId())))
                .filter(Predicate.not(post -> postsToFilter.contains(post.getId())))
                .map(postMapper::mapExternal)
                .collect(Collectors.toList());
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
    public void setEnvironmentProperties(EnvironmentProperties environmentProperties)
    {
        this.environmentProperties = environmentProperties;
    }
}

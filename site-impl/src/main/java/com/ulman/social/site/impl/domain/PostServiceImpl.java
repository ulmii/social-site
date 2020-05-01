package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.service.PostService;
import com.ulman.social.site.impl.configuration.EnvironmentProperties;
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
        postHelper.checkAccessIfPrivateProfile(userId);

        List<Post> userPosts = postRepository.findByUser_Id(userId);
        return userPosts.stream()
                .map(postMapper::mapExternal)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto addUserPost(String userId, PostDto postDto)
    {
        User user = userHelper.authorizeAndGetUserById(userId);

        Post post = postMapper.mapInternal(postDto);
        post.setUser(user);
        return postMapper.mapExternal(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostDto updatePost(String userId, String postId, PostDto postDto)
    {
        userHelper.authorizeAndGetUserById(userId, "Only account owners can update their posts");

        Post post = postHelper.getPostFromRepositoryByUserId(userId, postId);

        return postMapper.mapExternal(postHelper.updatePostWithPostDto(post, postDto));
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public PostDto getPost(String userId, String postId)
    {
        postHelper.checkAccessIfPrivateProfile(userId);

        Post post = postHelper.getPostFromRepositoryByUserId(userId, postId);

        return postMapper.mapExternal(post);
    }

    @Override
    @Transactional
    public PostDto deletePost(String userId, String postId)
    {
        userHelper.authorizeAndGetUserById(userId, "Only account owners can delete their posts");

        Post post = postHelper.getPostFromRepositoryByUserId(userId, postId);
        postRepository.delete(post);

        return postMapper.mapExternal(post);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<PostDto> getFollowingPosts(String userId)
    {
        userHelper.authorizeAndGetUserById(userId, "Only account owners can see their following users post collection");

        List<Post> userFollowingPosts = postRepository.getUserFollowingPosts(userId);
        return userFollowingPosts.stream()
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

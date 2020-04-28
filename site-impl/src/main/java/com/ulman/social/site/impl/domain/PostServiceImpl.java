package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.service.PostService;
import com.ulman.social.site.impl.configuration.EnvironmentProperties;
import com.ulman.social.site.impl.domain.error.exception.authentication.AuthenticationException;
import com.ulman.social.site.impl.domain.error.exception.post.PostDoesntExistException;
import com.ulman.social.site.impl.domain.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.domain.mapper.PostMapper;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.repository.PostRepository;
import com.ulman.social.site.impl.repository.UserRepository;
import com.ulman.social.site.impl.helper.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ulman.social.site.impl.domain.mapper.PostMapper.mapExternal;
import static com.ulman.social.site.impl.domain.mapper.PostMapper.mapInternal;
import static com.ulman.social.site.impl.domain.mapper.PostMapper.mapInternalPostId;

@Service
public class PostServiceImpl implements PostService
{
    private PostRepository postRepository;
    private UserRepository userRepository;
    private UserHelper userHelper;
    private EnvironmentProperties environmentProperties;

    @Autowired
    public PostServiceImpl(
            UserRepository userRepository, PostRepository postRepository)
    {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<PostDto> getUserPosts(String id)
    {
        if(!userRepository.existsById(id))
        {
            throw new UserDoesntExistException(String.format("User with id: [%s] does not exist", id));
        }

        Set<Post> userPosts = postRepository.findByUser_Id(id);

        return userPosts.stream()
                .map(PostMapper::mapExternal)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto addUserPost(String id, PostDto postDto)
    {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent())
        {
            if (userHelper.loggedUserIdMatchesWithRequest(id))
            {
                Post post = mapInternal(postDto);
                post.setUser(user.get());
                return mapExternal(postRepository.save(post));
            }
            else
            {
                throw new AuthenticationException("Only account owners can add posts");
            }
        }
        else
        {
            throw new UserDoesntExistException(String.format("User with id: [%s] does not exist", id));
        }
    }

    @Override
    public PostDto updatePost(String userId, String postId, PostDto postDto)
    {
        Optional<Post> post = postRepository.findById(mapInternalPostId(postId));
        return null;

    }

    @Override
    public PostDto getPost(String postId)
    {
        Optional<Post> post = postRepository.findById(mapInternalPostId(postId));

        if (post.isPresent())
        {
            return mapExternal(post.get());
        }
        else
        {
            throw new PostDoesntExistException(String.format("Post with id: [%s] does not exist", postId));
        }
    }

    @Override
    public List<PostDto> getFollowingPosts(String id)
    {
        return null;
    }

    @Autowired
    public void setUserHelper(UserHelper userHelper)
    {
        this.userHelper = userHelper;
    }

    @Autowired
    public void setEnvironmentProperties(EnvironmentProperties environmentProperties)
    {
        this.environmentProperties = environmentProperties;
    }
}

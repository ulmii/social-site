package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.api.service.PostService;
import com.ulman.social.site.impl.configuration.EnvironmentProperties;
import com.ulman.social.site.impl.domain.mapper.PostMapper;
import com.ulman.social.site.impl.error.exception.authentication.AuthenticationException;
import com.ulman.social.site.impl.error.exception.user.UserDoesntExistException;
import com.ulman.social.site.impl.model.db.User;
import com.ulman.social.site.impl.repository.PostRepository;
import com.ulman.social.site.impl.repository.UserRepository;
import com.ulman.social.site.impl.service.LoggedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ulman.social.site.impl.domain.mapper.PostMapper.mapInternal;

@Service
public class PostServiceImpl implements PostService
{
    @PersistenceContext
    private EntityManager entityManager;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private LoggedUserService loggedUserService;
    private EnvironmentProperties environmentProperties;

    @Autowired
    public PostServiceImpl(UserRepository userRepository, PostRepository postRepository)
    {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<PostDto> getUserPosts(String id)
    {
        return postRepository.findByUser_Id(id).stream()
                .map(PostMapper::mapExternal)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostDto addUserPost(String id, PostDto postDto)
    {
        Optional<User> wrappedUser = userRepository.findById(id);

        if (wrappedUser.isPresent())
        {
            if (loggedUserService.loggedUserIdMatchesWithRequest(id))
            {
                User user = wrappedUser.get();
                user.addPost(mapInternal(postDto));
                return postDto;
                //                return mapExternal(postRepository.save(mapInternal(postDto, user.get())));
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

    @Autowired
    public void setLoggedUserService(LoggedUserService loggedUserService)
    {
        this.loggedUserService = loggedUserService;
    }

    @Autowired
    public void setEnvironmentProperties(EnvironmentProperties environmentProperties)
    {
        this.environmentProperties = environmentProperties;
    }

}

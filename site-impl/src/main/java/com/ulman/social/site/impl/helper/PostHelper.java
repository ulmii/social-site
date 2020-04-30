package com.ulman.social.site.impl.helper;

import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.impl.domain.error.exception.authentication.PrivateProfileException;
import com.ulman.social.site.impl.domain.error.exception.post.ImmutablePostFieldException;
import com.ulman.social.site.impl.domain.error.exception.post.PostDoesntExistException;
import com.ulman.social.site.impl.domain.mapper.PostMapper;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PostHelper
{
    private UserHelper userHelper;
    private PostMapper postMapper;
    private PostRepository postRepository;

    @Autowired
    public PostHelper(UserHelper userHelper, PostMapper postMapper, PostRepository postRepository)
    {
        this.userHelper = userHelper;
        this.postMapper = postMapper;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Post getPostFromRepository(String id)
    {
        Optional<Post> post = postRepository.findById(postMapper.mapInternalPostId(id));

        if (post.isEmpty())
        {
            throw new PostDoesntExistException(String.format("Post with id: [%s] does not exist", id));
        }

        return post.get();
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Post getPostFromRepositoryByUserId(String userId, String postId)
    {
        UUID postUuid = postMapper.mapInternalPostId(postId);
        Optional<Post> postFromRepository = postRepository.findByUser_Id(userId).stream()
                .filter(post -> post.getId().equals(postUuid))
                .findFirst();

        if (postFromRepository.isEmpty())
        {
            if(postRepository.existsById(postUuid))
            {
                throw new PostDoesntExistException(String.format("Post with id: [%s] is assigned to different user", postId));
            }

            throw new PostDoesntExistException(String.format("Post with id: [%s] does not exist", postId));
        }

        return postFromRepository.get();
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public void checkAccessIfPrivateProfile(String userId)
    {
        User user = userHelper.getUserFromRepository(userId);

        if (!user.getPublicProfile())
        {
            User loggedUser = userHelper.getLoggedUser();
            if (!user.getId().equals(loggedUser.getId()) && !user.getFollowers().contains(loggedUser))
            {
                throw new PrivateProfileException(String.format("You must be one of [%s] followers to view posts", user.getId()));
            }
        }
    }

    public Post updatePostWithPostDto(Post post, PostDto postDto)
    {
        if (Objects.nonNull(postDto.getId()))
        {
            throw new ImmutablePostFieldException("Field [id] in post can't be updated");
        }

        if (Objects.nonNull(postDto.getUserId()))
        {
            throw new ImmutablePostFieldException("Field [userId] in post can't be updated");
        }

        if (Objects.nonNull(postDto.getCreated()))
        {
            throw new ImmutablePostFieldException("Field [created] in post can't be updated");
        }

        if (Objects.nonNull(postDto.getDescription()))
        {
            post.setDescription(postDto.getDescription());
        }

        if (Objects.nonNull(postDto.getPhotos()))
        {
            post.setPhotos(null);
        }

        return postRepository.save(post);
    }
}

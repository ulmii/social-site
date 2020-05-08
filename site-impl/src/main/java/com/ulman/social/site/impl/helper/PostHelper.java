package com.ulman.social.site.impl.helper;

import com.devskiller.friendly_id.FriendlyId;
import com.ulman.social.site.api.model.PostDto;
import com.ulman.social.site.impl.domain.error.exception.post.ImmutablePostFieldException;
import com.ulman.social.site.impl.domain.error.exception.post.PostDoesntExistException;
import com.ulman.social.site.impl.domain.mapper.PostMapper;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class PostHelper
{
    private PostMapper postMapper;
    private PostRepository postRepository;

    @Autowired
    public PostHelper(PostMapper postMapper, PostRepository postRepository)
    {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Post getPostFromRepository(String id)
    {
        Optional<Post> post = postRepository.findById(FriendlyId.toUuid(id));

        if (post.isEmpty())
        {
            throw new PostDoesntExistException(String.format("Post with id: [%s] does not exist", id));
        }

        return post.get();
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Post getPostByUserIdAndPostId(String userId, String postId)
    {
        UUID postUuid = FriendlyId.toUuid(postId);
        Optional<Post> postFromRepository = postRepository.getPostByUserIdAndPostId(userId, postUuid);

        if (postFromRepository.isEmpty())
        {
            if (postRepository.existsById(postUuid))
            {
                throw new PostDoesntExistException(String.format("Post with id: [%s] is assigned to different user", postId));
            }

            throw new PostDoesntExistException(String.format("Post with id: [%s] does not exist", postId));
        }

        return postFromRepository.get();
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
            post.setPhotos(postMapper.mapBase64ListToBlobList(postDto.getPhotos()));
        }

        postRepository.save(post);
        return postRepository.findById(post.getId()).get();
    }
}

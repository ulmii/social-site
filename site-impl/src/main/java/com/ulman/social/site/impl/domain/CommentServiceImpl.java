package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.api.service.CommentService;
import com.ulman.social.site.impl.domain.mapper.CommentMapper;
import com.ulman.social.site.impl.domain.model.db.Comment;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.helper.CommentHelper;
import com.ulman.social.site.impl.helper.PostHelper;
import com.ulman.social.site.impl.helper.UserHelper;
import com.ulman.social.site.impl.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService
{
    private CommentRepository commentRepository;
    private UserHelper userHelper;
    private PostHelper postHelper;
    private CommentHelper commentHelper;
    private CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentDto> getComments(String userId, String postId)
    {
        List<Comment> postComments = commentHelper.getPostComments(userId, postId);

        return postComments.stream()
                .map(commentMapper::mapExternal)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(String userId, String postId, CommentDto commentDto)
    {
        User loggedUser = userHelper.getLoggedUser();
        postHelper.checkAccessIfPrivateProfile(userId);

        Post post = postHelper.getPostFromRepositoryByUserId(userId, postId);

        Comment comment = commentMapper.mapInternal(commentDto, post, loggedUser);
        return commentMapper.mapExternal(commentRepository.save(comment));
    }

    @Autowired
    public void setUserHelper(UserHelper userHelper)
    {
        this.userHelper = userHelper;
    }

    @Autowired
    public void setPostHelper(PostHelper postHelper)
    {
        this.postHelper = postHelper;
    }

    @Autowired
    public void setCommentHelper(CommentHelper commentHelper)
    {
        this.commentHelper = commentHelper;
    }

    @Autowired
    public void setCommentMapper(CommentMapper commentMapper)
    {
        this.commentMapper = commentMapper;
    }
}

package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.api.service.CommentService;
import com.ulman.social.site.impl.domain.error.exception.authentication.PrivateProfileException;
import com.ulman.social.site.impl.domain.mapper.CommentMapper;
import com.ulman.social.site.impl.domain.model.db.Comment;
import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.helper.CommentHelper;
import com.ulman.social.site.impl.helper.PostHelper;
import com.ulman.social.site.impl.helper.UserHelper;
import com.ulman.social.site.impl.repository.CommentRepository;
import com.ulman.social.site.impl.repository.OffsetPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
    public Page<CommentDto> getComments(String userId, String postId, int limit, int offset)
    {
        if (userHelper.isProfileNotAccessible(userId))
        {
            throw new PrivateProfileException(String.format("You must be one of [%s] followers to access comments", userId));
        }

        OffsetPageRequest offsetPageRequest = OffsetPageRequest.of(limit, offset);

        Page<Comment> postComments = commentHelper.getPostComments(userId, postId, offsetPageRequest);

        return commentMapper.mapEntityPageIntoDtoPage(offsetPageRequest, postComments);
    }

    @Override
    public CommentDto addComment(String userId, String postId, CommentDto commentDto)
    {
        User loggedUser = userHelper.getLoggedUser();
        if (userHelper.isProfileNotAccessible(userId))
        {
            throw new PrivateProfileException(String.format("You must be one of [%s] followers to add a comment", userId));
        }

        Post post = postHelper.getPostByUserIdAndPostId(userId, postId);

        Comment comment = commentMapper.mapInternal(commentDto, post, loggedUser);
        return commentMapper.mapExternal(commentRepository.save(comment));
    }

    @Override
    public CommentDto getComment(String userId, String postId, String commentId)
    {
        if (userHelper.isProfileNotAccessible(userId))
        {
            throw new PrivateProfileException(String.format("You must be one of [%s] followers to access comments", userId));
        }

        Comment comment = commentHelper.getCommentByUserIdAndPostIdAndCommentId(userId, postId, commentId);

        return commentMapper.mapExternal(comment);
    }

    @Override
    public CommentDto updateComment(String userId, String postId, String commentId, CommentDto commentDto)
    {
        Comment comment = commentHelper.getCommentByUserIdAndPostIdAndCommentId(userId, postId, commentId);
        userHelper.authorizeAndGetUserById(comment.getUser().getId(), "Only account owners can update their comments");

        return commentMapper.mapExternal(commentHelper.updateCommentWithCommentDto(comment, commentDto));
    }

    @Override
    public CommentDto deleteComment(String userId, String postId, String commentId)
    {
        Comment comment = commentHelper.getCommentByUserIdAndPostIdAndCommentId(userId, postId, commentId);
        userHelper.authorizeAndGetUserById(comment.getUser().getId(), "Only account owners can delete their comments");

        commentRepository.delete(comment);

        return commentMapper.mapExternal(comment);
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

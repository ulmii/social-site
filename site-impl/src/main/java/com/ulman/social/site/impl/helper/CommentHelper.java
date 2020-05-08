package com.ulman.social.site.impl.helper;

import com.devskiller.friendly_id.FriendlyId;
import com.ulman.social.site.api.model.CommentDto;
import com.ulman.social.site.impl.domain.error.exception.authentication.PrivateProfileException;
import com.ulman.social.site.impl.domain.error.exception.comment.CommentDoesntExistException;
import com.ulman.social.site.impl.domain.error.exception.comment.ImmutableCommentFieldException;
import com.ulman.social.site.impl.domain.model.db.Comment;
import com.ulman.social.site.impl.repository.CommentRepository;
import com.ulman.social.site.impl.repository.OffsetPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class CommentHelper
{
    private CommentRepository commentRepository;
    private UserHelper userHelper;
    private PostHelper postHelper;

    @Autowired
    public CommentHelper(CommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Page<Comment> getPostComments(String userId, String postId, OffsetPageRequest offsetPageRequest)
    {
        if (userHelper.isProfileNotAccessible(userId))
        {
            throw new PrivateProfileException(String.format("You must be one of [%s] followers to view posts", userId));
        }
        postHelper.getPostByUserIdAndPostId(userId, postId);

        UUID postUuid = FriendlyId.toUuid(postId);

        return commentRepository.getPostComments(postUuid, offsetPageRequest);
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Comment getCommentByUserIdAndPostIdAndCommentId(String userId, String postId, String commentId)
    {
        UUID postUuid = FriendlyId.toUuid(postId);
        UUID commentUuid = FriendlyId.toUuid(commentId);
        Optional<Comment> comment = commentRepository.getPostByUserIdAndPostIAndCommentId(userId, postUuid, commentUuid);

        if (comment.isEmpty())
        {
            if (commentRepository.existsById(commentUuid))
            {
                throw new CommentDoesntExistException(String.format("Comment with id: [%s] is assigned to different post", commentUuid));
            }

            throw new CommentDoesntExistException(String.format("Comment with id: [%s] does not exist", commentUuid));
        }

        return comment.get();
    }

    public Comment updateCommentWithCommentDto(Comment comment, CommentDto commentDto)
    {
        if (Objects.nonNull(commentDto.getId()))
        {
            throw new ImmutableCommentFieldException("Field [id] in comment can't be updated");
        }

        if (Objects.nonNull(commentDto.getPostId()))
        {
            throw new ImmutableCommentFieldException("Field [postId] in comment can't be updated");
        }

        if (Objects.nonNull(commentDto.getUserId()))
        {
            throw new ImmutableCommentFieldException("Field [userId] in comment can't be updated");
        }

        if (Objects.nonNull(commentDto.getCreated()))
        {
            throw new ImmutableCommentFieldException("Field [created] in comment can't be updated");
        }

        if (Objects.nonNull(commentDto.getUpdated()))
        {
            throw new ImmutableCommentFieldException("Field [updated] in comment can't be updated");
        }

        if (Objects.nonNull(commentDto.getRootLevel()))
        {
            throw new ImmutableCommentFieldException("Field [rootLevel] in comment can't be updated");
        }

        if (Objects.nonNull(commentDto.getContent()))
        {
            comment.setContent(commentDto.getContent());
        }

        commentRepository.save(comment);
        return commentRepository.findById(comment.getId()).get();
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
}

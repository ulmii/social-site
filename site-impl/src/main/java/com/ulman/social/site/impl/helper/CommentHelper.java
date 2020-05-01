package com.ulman.social.site.impl.helper;

import com.ulman.social.site.impl.domain.mapper.PostMapper;
import com.ulman.social.site.impl.domain.model.db.Comment;
import com.ulman.social.site.impl.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class CommentHelper
{
    private CommentRepository commentRepository;
    private PostMapper postMapper;
    private PostHelper postHelper;

    @Autowired
    public CommentHelper(CommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<Comment> getPostComments(String userId, String postId)
    {
        postHelper.checkAccessIfPrivateProfile(userId);
        postHelper.getPostFromRepositoryByUserId(userId, postId);

        UUID postUuid = postMapper.mapInternalPostId(postId);

        return commentRepository.getPostComments(postUuid);
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
}

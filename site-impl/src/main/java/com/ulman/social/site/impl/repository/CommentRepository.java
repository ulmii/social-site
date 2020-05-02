package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.domain.model.db.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID>
{
    @Query("SELECT DISTINCT comment FROM Comment comment "
            + "LEFT JOIN FETCH comment.post post "
            + "WHERE post.id = (:postId) "
            + "ORDER BY comment.created DESC")
    List<Comment> getPostComments(UUID postId);

    @Query("SELECT comment FROM Comment comment "
            + "LEFT JOIN FETCH comment.post post "
            + "WHERE post.user.id = (:userId) "
            + "AND post.id = (:postId) "
            + "AND comment.id = (:commentId)")
    Optional<Comment> getPostByUserIdAndPostIAndCommentId(String userId, UUID postId, UUID commentId);
}

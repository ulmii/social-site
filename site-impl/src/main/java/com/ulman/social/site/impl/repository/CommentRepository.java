package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.domain.model.db.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, UUID>
{
    @Query("SELECT DISTINCT comment FROM Comment comment "
            + "LEFT JOIN comment.post post "
            + "WHERE post.id = (:postId)")
    Page<Comment> getPostComments(UUID postId, Pageable pageable);

    @Query("SELECT comment FROM Comment comment "
            + "LEFT JOIN FETCH comment.post post "
            + "WHERE post.user.id = (:userId) "
            + "AND post.id = (:postId) "
            + "AND comment.id = (:commentId)")
    Optional<Comment> getPostByUserIdAndPostIAndCommentId(String userId, UUID postId, UUID commentId);
}

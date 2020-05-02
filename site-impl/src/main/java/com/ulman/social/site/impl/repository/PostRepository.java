package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.domain.model.db.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>
{
    @Query("SELECT post FROM Post post "
            + "LEFT JOIN FETCH post.photos "
            + "WHERE post.id = (:postId)")
    Optional<Post> findById(UUID postId);

    @Query("SELECT post FROM Post post "
            + "LEFT JOIN FETCH post.photos "
            + "WHERE post.user.id = (:userId)"
            + "ORDER BY post.created DESC")
    List<Post> getPostsByUserId(String userId);

    @Query("SELECT post FROM Post post "
            + "LEFT JOIN FETCH post.photos "
            + "WHERE post.user.id = (:userId) "
            + "AND post.id = (:postId) "
            + "ORDER BY post.created DESC")
    Optional<Post> getPostByUserIdAndPostId(String userId, UUID postId);

    @Query("SELECT DISTINCT post FROM Post post "
            + "LEFT JOIN FETCH post.user users "
            + "LEFT JOIN FETCH post.photos photos "
            + "LEFT JOIN FETCH users.followers followers "
            + "WHERE followers.id = (:userId) "
            + "ORDER BY post.created DESC")
    List<Post> getUserFollowingPosts(String userId);
}

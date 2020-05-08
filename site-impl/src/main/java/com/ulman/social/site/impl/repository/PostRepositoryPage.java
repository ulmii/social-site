package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.domain.model.db.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepositoryPage extends Repository<Post, UUID>
{
    @Query("SELECT post FROM Post post "
            + "LEFT JOIN FETCH post.photos "
            + "WHERE post.id = (:postId)")
    Optional<Post> findById(UUID postId);

    @Query(value = "SELECT post FROM Post post "
            + "LEFT JOIN post.photos "
            + "WHERE post.user.id = (:userId)",
            countQuery = "SELECT count(post) FROM Post post WHERE post.user.id=(:userId) group by post.id")
    Page<Post> getPostsByUserId(@Param("userId") String userId, Pageable pageRequest);

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

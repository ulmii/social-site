package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.domain.model.db.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>
{
    @Query("SELECT post FROM Post post LEFT JOIN FETCH post.photos WHERE post.id = (:id)")
    Optional<Post> findById(UUID id);

    @Query("SELECT post FROM Post post LEFT JOIN FETCH post.photos WHERE post.user.id = (:userId)")
    Set<Post> findByUser_Id(String userId);
}

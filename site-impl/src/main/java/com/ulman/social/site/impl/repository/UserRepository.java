package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.domain.model.db.Post;
import com.ulman.social.site.impl.domain.model.db.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String>
{
    Optional<User> findByEmail(String email);

    @Query("SELECT user.followers FROM User user WHERE user.id=(:userId)")
    Page<User> getFollowers(String userId, Pageable pageable);

    @Query("SELECT user.following FROM User user WHERE user.id=(:userId)")
    Page<User> getFollowing(String userId, Pageable pageable);

    @Query("SELECT user.pendingFollowers FROM User user WHERE user.id=(:userId)")
    Page<User> getPendingFollowers(String userId, Pageable pageable);

    @Query("SELECT user.hiddenUsers FROM User user WHERE user.id=(:userId)")
    Page<User> getHiddenUsers(String userId, Pageable pageable);

    @Query("SELECT user.hiddenPosts FROM User user WHERE user.id=(:userId)")
    Page<Post> getHiddenPosts(String userId, Pageable pageable);

    @Query("SELECT user.savedUsers FROM User user WHERE user.id=(:userId)")
    Page<User> getSavedUsers(String userId, Pageable pageable);

    @Query("SELECT user.savedPosts FROM User user WHERE user.id=(:userId)")
    Page<Post> getSavedPosts(String userId, Pageable pageable);
}

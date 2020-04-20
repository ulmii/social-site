package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.model.db.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    List<Post> findByUser_Id(String userId);
}

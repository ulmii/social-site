package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{
    User findByEmail(String username);
}

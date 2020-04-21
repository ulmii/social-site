package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.domain.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{
    Optional<User> findByEmail(String email);
}

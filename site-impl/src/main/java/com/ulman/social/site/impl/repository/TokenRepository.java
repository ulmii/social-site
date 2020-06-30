package com.ulman.social.site.impl.repository;

import com.ulman.social.site.impl.domain.model.db.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<User, String>
{
    @Query(value = "SELECT user FROM User user LEFT JOIN FETCH user.invalidatedTokens WHERE user.email=(:email)")
    Optional<User> getUserWithTokens(String email);

    @Query(value = "SELECT tokens FROM User user LEFT JOIN user.invalidatedTokens tokens WHERE user.email=(:email)")
    List<String> getInvalidatedTokens(String email);
}

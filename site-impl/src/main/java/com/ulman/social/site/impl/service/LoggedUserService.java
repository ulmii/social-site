package com.ulman.social.site.impl.service;

import com.ulman.social.site.impl.model.db.User;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoggedUserService
{
    private UserRepository userRepository;

    @Autowired
    public LoggedUserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public Optional<User> getLoggedUser()
    {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal);
    }

    public boolean loggedUserIdMatchesWithRequest(String id)
    {
        Optional<User> loggedUser = getLoggedUser();

        return loggedUser.isPresent() && loggedUser.get().getId().equals(id);
    }
}

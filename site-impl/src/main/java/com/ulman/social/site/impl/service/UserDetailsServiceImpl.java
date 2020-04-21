package com.ulman.social.site.impl.service;

import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
    {
        Optional<User> applicationUser = userRepository.findByEmail(email);
        if (applicationUser.isPresent())
        {
            User user = applicationUser.get();

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), emptyList());
        }

        throw new UsernameNotFoundException(email);
    }
}

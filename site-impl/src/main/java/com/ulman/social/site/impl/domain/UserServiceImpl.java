package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.UserService;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.model.db.User;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> getUsers()
    {
        return userRepository.findAll().stream()
                .map(UserMapper::mapInternal)
                .collect(Collectors.toList());
    }

    @Override
    public void addUser(UserDto userDto)
    {
        if(accountExists(userDto.getId()))
        {
            throw new RuntimeException();
        }

        userRepository.save(User.builder()
                .withId(userDto.getId())
                .withEmail(userDto.getEmail())
                .withPassword(passwordEncoder.encode(userDto.getPassword()))
                .withPublicProfile(userDto.isPublicProfile())
                .build());
    }

    @Override
    public UserDto getUser(String id)
    {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userRepository.findByEmail(principal).getId();

        if(userId.equals(id))
        {
            return UserMapper.mapInternal(userRepository.findById(id).get());
        }
        else
        {
            throw new RuntimeException();
        }
    }

    @Override
    public void updateUser(UserDto userDto)
    {

    }

    @Override
    public List<UserDto> getFollowers(String id)
    {
        return null;
    }

    @Override
    public List<UserDto> getFollowing(String id)
    {
        return null;
    }

    @Override
    public void addFollower(String id, String id2)
    {

    }

    private boolean accountExists(String id)
    {
        return userRepository.findById(id).isPresent();
    }
}

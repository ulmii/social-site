package com.ulman.social.site.impl.domain;

import com.ulman.social.site.api.model.UserDto;
import com.ulman.social.site.api.service.LogoutService;
import com.ulman.social.site.impl.domain.error.exception.InvalidTokenException;
import com.ulman.social.site.impl.domain.error.exception.authentication.UserNotLoggedInException;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.helper.UserHelper;
import com.ulman.social.site.impl.repository.TokenRepository;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogoutServiceImpl implements LogoutService
{
    private TokenRepository tokenRepository;
    private UserHelper userHelper;
    private UserMapper userMapper;

    @Autowired
    public LogoutServiceImpl(TokenRepository tokenRepository)
    {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional(noRollbackFor = InvalidTokenException.class)
    public UserDto logout(String token)
    {
        if (StringUtils.isBlank(token))
        {
            throw new UserNotLoggedInException("You must be logged in to access this resource");
        }

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (email.matches("anonymousUser"))
        {
            throw new UserNotLoggedInException("You must be logged in to access this resource");
        }

        String jwtToken = token.split(" ")[1];
        User user = tokenRepository.getUserWithTokens(email).get();

        if (user.getInvalidatedTokens().contains(jwtToken))
        {
            throw new InvalidTokenException("Token already invalidated");
        }

        user.invalidateToken(jwtToken);
        return userMapper.mapExternal(user);
    }

    @Autowired
    public void setUserHelper(UserHelper userHelper)
    {
        this.userHelper = userHelper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper)
    {
        this.userMapper = userMapper;
    }
}

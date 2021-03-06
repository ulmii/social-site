package com.ulman.social.site.impl.security.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulman.social.site.impl.configuration.EnvironmentProperties;
import com.ulman.social.site.impl.domain.error.exception.authentication.AuthorizationException;
import com.ulman.social.site.impl.domain.error.exception.model.JsonResponse;
import com.ulman.social.site.impl.domain.error.exception.util.ResponseUtil;
import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.repository.TokenRepository;
import com.ulman.social.site.impl.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    private AuthenticationManager authenticationManager;
    private EnvironmentProperties environmentProperties;
    private UserRepository userRepository;
    private UserMapper userMapper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, EnvironmentProperties environmentProperties, UserRepository userRepository)
    {
        this.authenticationManager = authenticationManager;
        this.environmentProperties = environmentProperties;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
            HttpServletResponse res) throws AuthenticationException
    {
        try
        {
            User user = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        }
        catch (IOException e)
        {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth) throws IOException
    {
        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + environmentProperties.getSecurity().getExpirationTime()))
                .sign(HMAC512(environmentProperties.getSecurity().getSecret().getBytes()));
        response.addHeader(environmentProperties.getSecurity().getHeaderString(), environmentProperties.getSecurity().getTokenPrefix() + token);

        Optional<User> user = userRepository.findByEmail(auth.getName());

        ResponseUtil
                .sendJsonResponse(environmentProperties.getTimeZone(), response, new JsonResponse(userMapper.mapExternal(user.get()), Response.Status.OK));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException
    {
        SecurityContextHolder.clearContext();

        AuthorizationException badCredentialsException = new AuthorizationException(
                environmentProperties.getTimeZone().toZoneId(), environmentProperties.getApiVersion(), failed);
        JsonResponse jsonResponse = new JsonResponse(badCredentialsException, badCredentialsException.getError().getStatus());

        ResponseUtil
                .sendJsonResponse(environmentProperties.getTimeZone(), response, jsonResponse);
    }

    public void setUserMapper(UserMapper userMapper)
    {
        this.userMapper = userMapper;
    }
}

package com.ulman.social.site.impl.security.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulman.social.site.impl.configuration.EnvironmentProperties;
import com.ulman.social.site.impl.domain.error.exception.authentication.AuthorizationException;
import com.ulman.social.site.impl.domain.model.db.User;
import com.ulman.social.site.impl.security.response.model.JsonResponse;
import com.ulman.social.site.impl.security.util.AuthenticationResponseUtil;
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
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    private AuthenticationManager authenticationManager;
    private EnvironmentProperties environmentProperties;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, EnvironmentProperties environmentProperties)
    {
        this.authenticationManager = authenticationManager;
        this.environmentProperties = environmentProperties;
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
            throw new RuntimeException(e);
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
        Cookie cookie = new Cookie("token", token);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        AuthenticationResponseUtil
                .sendJsonResponse(response, new JsonResponse(auth.getPrincipal(), Response.Status.OK));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException
    {
        SecurityContextHolder.clearContext();

        AuthorizationException badCredentialsException = new AuthorizationException(environmentProperties.getApiVersion(), failed);
        JsonResponse jsonResponse = new JsonResponse(badCredentialsException, badCredentialsException.getError().getStatus());

        AuthenticationResponseUtil
                .sendJsonResponse(response, jsonResponse);
    }
}

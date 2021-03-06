package com.ulman.social.site.impl.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ulman.social.site.impl.configuration.EnvironmentProperties;
import com.ulman.social.site.impl.domain.error.exception.authentication.AuthorizationException;
import com.ulman.social.site.impl.domain.error.exception.model.JsonResponse;
import com.ulman.social.site.impl.domain.error.exception.util.ResponseUtil;
import com.ulman.social.site.impl.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter
{
    private EnvironmentProperties environmentProperties;
    private TokenRepository tokenRepository;

    public JWTAuthorizationFilter(AuthenticationManager authManager, TokenRepository tokenRepository, EnvironmentProperties environmentProperties)
    {
        super(authManager);
        this.tokenRepository = tokenRepository;
        this.environmentProperties = environmentProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {
        String header = request.getHeader(environmentProperties.getSecurity().getHeaderString());

        if (header == null || !header.startsWith(environmentProperties.getSecurity().getTokenPrefix()))
        {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication;
        try
        {
            authentication = getAuthentication(request);
        }
        catch (JWTVerificationException e)
        {
            SecurityContextHolder.clearContext();

            AuthorizationException authorizationException = new AuthorizationException(
                    environmentProperties.getTimeZone().toZoneId(), environmentProperties.getApiVersion(), e);
            JsonResponse jsonResponse = new JsonResponse(authorizationException, authorizationException.getError().getStatus());

            ResponseUtil.sendJsonResponse(environmentProperties.getTimeZone(), response, jsonResponse);

            log.error(e.getMessage());
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request)
    {
        String token = request.getHeader(environmentProperties.getSecurity().getHeaderString());
        if (token != null)
        {
            token = token.replace(environmentProperties.getSecurity().getTokenPrefix(), "");

            String email = JWT.require(Algorithm.HMAC512(environmentProperties.getSecurity().getSecret().getBytes()))
                    .build()
                    .verify(token.replace(environmentProperties.getSecurity().getTokenPrefix(), ""))
                    .getSubject();

            if (email != null)
            {
                if (tokenRepository.getInvalidatedTokens(email).contains(token))
                {
                    return null;
                }

                return new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}

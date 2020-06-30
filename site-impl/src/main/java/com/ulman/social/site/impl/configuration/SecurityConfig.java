package com.ulman.social.site.impl.configuration;

import com.ulman.social.site.impl.domain.mapper.UserMapper;
import com.ulman.social.site.impl.repository.TokenRepository;
import com.ulman.social.site.impl.repository.UserRepository;
import com.ulman.social.site.impl.security.filter.JWTAuthenticationFilter;
import com.ulman.social.site.impl.security.filter.JWTAuthorizationFilter;
import com.ulman.social.site.impl.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private EnvironmentProperties environmentProperties;
    private TokenRepository tokenRepository;
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder,
            EnvironmentProperties environmentProperties,
            TokenRepository tokenRepository,
            UserRepository userRepository)
    {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.environmentProperties = environmentProperties;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors()
                .and().csrf().disable()
                .addFilter(getJWTAuthenticationFilter())
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), tokenRepository, environmentProperties))
                .authorizeRequests()
                .antMatchers("/api/v1/users", "/api/v1/users/**", "/api/v1/logout")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper)
    {
        this.userMapper = userMapper;
    }

    public JWTAuthenticationFilter getJWTAuthenticationFilter() throws Exception
    {
        final JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager(), environmentProperties, userRepository);
        filter.setFilterProcessesUrl("/api/v1/login");
        filter.setUserMapper(userMapper);
        return filter;
    }
}

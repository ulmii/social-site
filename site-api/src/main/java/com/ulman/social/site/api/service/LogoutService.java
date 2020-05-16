package com.ulman.social.site.api.service;

import com.ulman.social.site.api.model.UserDto;

public interface LogoutService
{
    UserDto logout(String token);
}

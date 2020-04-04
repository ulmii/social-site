package com.ulman.social.site.api.model;

import lombok.Data;
import lombok.NonNull;

import javax.ws.rs.DefaultValue;

@Data
public class UserDto
{
    @NonNull
    private String id;
    private String name;
    @NonNull
    private String email;
    private String password;
    private String photo;
    private String description;
    @DefaultValue("true")
    private boolean publicProfile;
}

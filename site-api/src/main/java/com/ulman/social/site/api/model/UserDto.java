package com.ulman.social.site.api.model;

import com.ulman.social.site.api.validation.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class UserDto
{
    @NotNull
    @NotEmpty
    private String id;
    private String name;
    @NotNull
    @NotEmpty
    private String email;
    @NotNull
    @NotEmpty
    private String password;
    private String photo;
    private String description;
    @DefaultValue("true")
    private boolean publicProfile;
}

package com.ulman.social.site.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.DefaultValue;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class UserDto
{
    @NotBlank
    private String id;
    private String name;
    @Email
    private String email;
    @NotBlank
    private String password;
    private String photo;
    private String description;
    @DefaultValue("true")
    private boolean publicProfile;
}

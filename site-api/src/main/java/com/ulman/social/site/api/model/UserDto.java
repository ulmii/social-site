package com.ulman.social.site.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ulman.social.site.api.validation.OnCreate;
import com.ulman.social.site.api.validation.OnUpdate;
import com.ulman.social.site.api.validation.user.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserDto
{
    @NotBlank(groups = OnCreate.class)
    private String id;
    private String name;
    @NotBlank(groups = OnCreate.class)
    @Email(message = "Invalid email", groups = { OnCreate.class, OnUpdate.class })
    private String email;
    @NotBlank(groups = OnCreate.class)
    @Password(groups = { OnCreate.class, OnUpdate.class })
    private String password;
    private String photo;
    private String description;
    private Boolean publicProfile;
}

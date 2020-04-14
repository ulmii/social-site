package com.ulman.social.site.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ulman.social.site.api.validation.Password;
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
    @NotBlank
    private String id;
    private String name;
    @Email(message = "Invalid email")
    @NotBlank
    private String email;
    @Password
    @NotBlank
    private String password;
    private String photo;
    private String description;
    private Boolean publicProfile;
}

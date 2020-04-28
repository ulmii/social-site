package com.ulman.social.site.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFollowing
{
    private UserDto user;
    private List<UserDto> following;

}

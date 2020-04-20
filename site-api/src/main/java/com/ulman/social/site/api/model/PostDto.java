package com.ulman.social.site.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto
{
    private Long id;
    private String userId;
    private String description;
}

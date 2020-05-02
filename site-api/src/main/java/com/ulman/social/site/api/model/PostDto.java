package com.ulman.social.site.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ulman.social.site.api.validation.Base64;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.sql.Timestamp;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PostDto
{
    @Null(message = "Changing/Specifying post id is forbidden")
    private String id;
    @Null(message = "Changing/Specifying userId is forbidden")
    private String userId;
    @Null(message = "Changing/Specifying creation date is forbidden")
    private Timestamp created;
    @Null(message = "Changing/Specifying update date is forbidden")
    private Timestamp updated;
    private String description;
    private List<@NotBlank @Base64 String> photos;
}

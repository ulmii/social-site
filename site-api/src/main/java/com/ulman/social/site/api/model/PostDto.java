package com.ulman.social.site.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ulman.social.site.api.validation.Base64;
import com.ulman.social.site.api.validation.OnCreate;
import com.ulman.social.site.api.validation.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PostDto implements Serializable
{
    @Null(message = "Changing/Specifying post id is forbidden")
    private String id;
    @Null(message = "Changing/Specifying userId is forbidden")
    private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Null(message = "Changing/Specifying creation date is forbidden")
    private Date created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Null(message = "Changing/Specifying update date is forbidden")
    private Date updated;
    @Size(max = 5000, groups = { OnCreate.class, OnUpdate.class })
    private String description;
    private List<@NotBlank @Base64 String> photos;

    @JsonIgnore
    @AssertTrue(message = "Post must not be empty")
    public boolean isNotEmpty()
    {
        return description != null || photos != null;
    }
}

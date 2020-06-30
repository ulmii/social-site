package com.ulman.social.site.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ulman.social.site.api.validation.OnCreate;
import com.ulman.social.site.api.validation.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto implements Serializable
{
    @Null(message = "Changing/Specifying comment id is forbidden")
    private String id;
    @Null(message = "Changing/Specifying userId is forbidden")
    private String userId;
    @Null(message = "Changing/Specifying postId is forbidden")
    private String postId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Null(message = "Changing/Specifying creation date is forbidden")
    private Date created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Null(message = "Changing/Specifying update date is forbidden")
    private Date updated;
    @Size(max = 256, groups = { OnCreate.class, OnUpdate.class })
    private String content;
}

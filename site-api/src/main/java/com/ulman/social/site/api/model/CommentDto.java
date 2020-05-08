package com.ulman.social.site.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.sql.Timestamp;

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
    @Null(message = "Changing/Specifying creation date is forbidden")
    private Timestamp created;
    @Null(message = "Changing/Specifying update date is forbidden")
    private Timestamp updated;
    @Null(message = "Changing/Specifying rootLevel is forbidden")
    private Boolean rootLevel;
    private String content;
}

package com.ulman.social.site.impl.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable
{
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String photo;
    private String description;
    private Boolean publicProfile;
}

package com.ulman.social.site.impl.domain.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Blob;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "users")
public class User implements Serializable
{
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    @Lob
    private Blob photo;
    private String description;
    private Boolean publicProfile;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Post> posts;
    @ManyToMany
    @JoinTable(name = "followers")
    private Set<User> followers;
    @ManyToMany
    @JoinTable(name = "following")
    private Set<User> following;

    public Post addPost(Post post)
    {
        posts.add(post);
        post.setUser(this);
        return post;
    }

    public Set<User> follow(User user)
    {
        following.add(user);
        user.getFollowers().add(this);
        return following;
    }
}

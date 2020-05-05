package com.ulman.social.site.impl.domain.model.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Container
{
    @OneToMany
    private List<User> users;
    @OneToMany
    private List<Post> posts;

    public Container addUser(User user)
    {
        users.add(user);
        return this;
    }

    public Container addPost(Post post)
    {
        posts.add(post);
        return this;
    }
}

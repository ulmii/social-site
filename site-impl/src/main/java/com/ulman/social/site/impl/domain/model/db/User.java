package com.ulman.social.site.impl.domain.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;
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
    @CreationTimestamp
    private Timestamp created;
    @UpdateTimestamp
    private Timestamp updated;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Post> posts;
    @ManyToMany
    @JoinTable(name = "followers",
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<User> followers;
    @ManyToMany
    @JoinTable(name = "pendingFollowers",
            inverseJoinColumns = @JoinColumn(name = "pendingFollower_id"))
    private Set<User> pendingFollowers;
    @ManyToMany
    @JoinTable(name = "following")
    private Set<User> following;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments;
    @ManyToMany
    @JoinTable(
            name = "hiddenUsers",
            inverseJoinColumns = @JoinColumn(name = "hiddenUser_id"))
    private Set<User> hiddenUsers;
    @OneToMany
    @JoinTable(
            name = "hiddenPosts",
            inverseJoinColumns = @JoinColumn(name = "hiddenPost_id"))
    private Set<Post> hiddenPosts;
    @ManyToMany
    @JoinTable(
            name = "savedUsers",
            inverseJoinColumns = @JoinColumn(name = "savedUser_id"))
    private Set<User> savedUsers;
    @OneToMany
    @JoinTable(name = "savedPosts",
            inverseJoinColumns = @JoinColumn(name = "savedPost_id"))
    private Set<Post> savedPosts;
    @ElementCollection
    @Column(name = "token")
    private List<String> invalidatedTokens;

    public void invalidateToken(String token)
    {
        invalidatedTokens.add(token);
    }

    public Set<User> follow(User user)
    {
        following.add(user);
        user.getFollowers().add(this);
        return following;
    }

    public Set<User> addPendingFollower(User user)
    {
        this.getPendingFollowers().add(user);
        return following;
    }

    public Set<User> acceptPendingFollower(User user)
    {
        pendingFollowers.remove(user);
        followers.add(user);
        user.getFollowing().add(this);
        return followers;
    }

    public User unfollow(User user)
    {
        following.remove(user);
        user.getFollowers().remove(this);
        return user;
    }

    public Comment addComment(Post post, Comment comment)
    {
        comments.add(comment);
        post.getComments().add(comment);
        comment.setPost(post);
        comment.setUser(this);

        return comment;
    }
}

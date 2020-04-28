package com.ulman.social.site.impl.domain.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Post")
@Table(name = "posts")
public class Post implements Serializable
{
    @Id
    @GeneratedValue
    @Column(updatable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private String description;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> photos;
    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp created;
    @UpdateTimestamp
    private Timestamp updated;
}

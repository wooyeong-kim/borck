package com.sparta.petplace.post.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "POST_ID")
    private Post post;
    @Column
    private String image;

    public PostImage(Post post, String image) {
        this.post = post;
        this.image = image;
    }
}

package com.sparta.petplace.post.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String category;
    @OneToMany
    private List<PostImage> images = new ArrayList<>();
    @Column(nullable = false)
    private String mapdata;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Integer telNum;
    @Column(nullable = false)
    private Integer startTime;
    @Column(nullable = false)
    private Integer endTime;
    @Column(nullable = false)
    private Integer closedDay;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public Post(String email, String title, String category, List<PostImage> images, String mapdata, String address, Integer telNum, Integer startTime, Integer endTime, Integer closedDay) {
        this.email = email;
        this.title = title;
        this.category = category;
        this.images = images;
        this.mapdata = mapdata;
        this.address = address;
        this.telNum = telNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.closedDay = closedDay;
    }
}

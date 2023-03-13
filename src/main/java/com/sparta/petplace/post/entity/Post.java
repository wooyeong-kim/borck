package com.sparta.petplace.post.entity;

import com.sparta.petplace.common.Timestamped;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Posts category;
    @Column(nullable = false)
    private String contents;
    @OneToMany
    private List<PostImage> images = new ArrayList<>();
    @Column(nullable = false)
    private String mapdata;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Integer telNum;
    @Column(nullable = false)
    private String ceo;
    @Column(nullable = false)
    private Integer startTime;
    @Column(nullable = false)
    private Integer endTime;
    @Column(nullable = false)
    private Integer closedDay;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID",nullable = false)
    private Member member;

    public Post(String email, String title, Posts category, String contents, String mapdata,
                String address, Integer telNum, String ceo,
                Integer startTime, Integer endTime, Integer closedDay, Member member) {
        this.email = email;
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.mapdata = mapdata;
        this.address = address;
        this.telNum = telNum;
        this.ceo = ceo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.closedDay = closedDay;
        this.member = member;
    }
}

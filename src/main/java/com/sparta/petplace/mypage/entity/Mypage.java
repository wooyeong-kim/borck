package com.sparta.petplace.mypage.entity;

import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Mypage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @Builder
    public Mypage(Member member, Post post){
        this.member = member;
        this.post = post;
    }
    public static Mypage of(Member member, Post post){
        return Mypage.builder()
                .member(member)
                .post(post)
                .build();
    }
}

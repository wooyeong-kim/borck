package com.sparta.petplace.member.entity;


import com.sparta.petplace.member.dto.SignupRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = true)
    private String business;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;


    @Builder
    public Member(String email,String password, String nickname, String business,LoginType loginType){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.business = business;
        this.loginType = loginType;
    }

    public void updateLoginStatus(LoginType loginType){
        this.loginType = loginType;
    }
}

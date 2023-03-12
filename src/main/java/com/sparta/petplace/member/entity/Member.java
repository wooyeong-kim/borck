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




    @Builder
    private Member(SignupRequestDto signupRequestDto) {
        password = signupRequestDto.getPassword();
        nickname = signupRequestDto.getNickname();
        email = signupRequestDto.getEmail();
    }

    public static Member of(SignupRequestDto signupRequestDto) {
        return Member.builder()
                .signupRequestDto(signupRequestDto)
                .build();
    }

}

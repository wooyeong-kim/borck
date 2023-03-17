package com.sparta.petplace.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialUserInfoDto {
    private Long id;
    private String email;
    private String nickname;
    private String image;

    public SocialUserInfoDto(Long id, String nickname, String email,String image) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.image = image;
    }
}
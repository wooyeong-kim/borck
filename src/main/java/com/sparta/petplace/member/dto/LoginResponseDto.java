package com.sparta.petplace.member.dto;

import com.sparta.petplace.member.entity.LoginType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    private String nickcame;
    private LoginType loginType;
    private SocialUserInfoDto socialUserInfoDto;
    @Builder
    public LoginResponseDto(String nickcame, LoginType loginType,SocialUserInfoDto socialUserInfoDto) {
        this.loginType = loginType;
        this.nickcame = nickcame;
        this.socialUserInfoDto = socialUserInfoDto;
    }

}

package com.sparta.petplace.member.dto;

import com.sparta.petplace.member.entity.LoginType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginResponseDto {
    private String nickcame;
    private LoginType loginType;
    private SocialUserInfoDto socialUserInfoDto;
    private String img;

    @Builder
    public LoginResponseDto(String nickcame, LoginType loginType, SocialUserInfoDto socialUserInfoDto, String img) {
        this.loginType = loginType;
        this.nickcame = nickcame;
        this.socialUserInfoDto = socialUserInfoDto;
        this.img = img;
    }

    public static LoginResponseDto of(String nickcame, LoginType loginType) {
       return LoginResponseDto.builder()
               .loginType(loginType)
               .nickcame(nickcame)
               .build();
    }
}


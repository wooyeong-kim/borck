package com.sparta.petplace.mypage.dto;

import com.sparta.petplace.mypage.entity.Mypage;
import lombok.Getter;

import java.util.List;

@Getter
public class MypageRequestDto {
    private List<Mypage> mypageList;
}

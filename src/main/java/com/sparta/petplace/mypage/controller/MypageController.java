package com.sparta.petplace.mypage.controller;


import com.sparta.petplace.auth.security.UserDetailsImpl;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.mypage.service.MypageService;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    @GetMapping("/mypage")
    public List<PostResponseDto> getView (@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getView(userDetails.getMember());
    }


}

package com.sparta.petplace.mypage.controller;

import com.sparta.petplace.auth.security.UserDetailsImpl;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.mypage.dto.MypageModifyRequestDto;
import com.sparta.petplace.mypage.service.MypageService;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    @GetMapping("/business")
    public List<PostResponseDto> getView (@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getView(userDetails.getMember());
    }
//    @GetMapping("/myreview")

//      찜 보여주기
    @GetMapping("/mypage/dibs")
    public List<PostResponseDto> getSave(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getSave(userDetails.getMember());
    }

    @PutMapping("/mypage/{user_id}")
    public ApiResponseDto<SuccessResponse> modify(@ModelAttribute MypageModifyRequestDto requestDto,
                                                         @PathVariable Long user_id,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.modify(requestDto, userDetails.getMember(), user_id);
    }
}

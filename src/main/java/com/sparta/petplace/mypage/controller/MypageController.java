package com.sparta.petplace.mypage.controller;

import com.sparta.petplace.auth.security.UserDetailsImpl;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.member.dto.MemberResponseDto;
import com.sparta.petplace.mypage.dto.MypageModifyRequestDto;
import com.sparta.petplace.mypage.service.MypageService;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import com.sparta.petplace.review.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

//      유저 정보
    @GetMapping("/mypage")
    public ApiResponseDto<MemberResponseDto> member(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getMember(userDetails.getMember());
    }


//      사업자 정보
    @GetMapping("/mypage/business")
    public List<PostResponseDto> getView (@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getView(userDetails.getMember());
    }
//    @GetMapping("/myreview")

//      찜 보여주기
    @GetMapping("/mypage/dibs")
    public List<PostResponseDto> getSave(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getSave(userDetails.getMember());
    }

    @PutMapping("/mypage")
    public ApiResponseDto<SuccessResponse> modify(@ModelAttribute MypageModifyRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.modify(requestDto, userDetails.getMember());
    }
//      사용자 보여주기
    @GetMapping("/myreview")
    public List<ReviewResponseDto> getReview (@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getReview(userDetails.getMember());
    }

}

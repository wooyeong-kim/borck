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
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;


    //유저 정보
    @GetMapping("/mypage")
    public ApiResponseDto<MemberResponseDto> member(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getMember(userDetails.getMember());
    }

    //유저 정보 수정
    @PatchMapping("/mypage")
    public ApiResponseDto<SuccessResponse> modify(@ModelAttribute MypageModifyRequestDto requestDto,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.modify(requestDto, userDetails.getMember());
    }

    //사업자가 작성한 게시글 조회
    @GetMapping("/mypage/business")
    public Page<PostResponseDto> getView (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestParam(value = "page") int page,
                                          @RequestParam(value = "size") int size){
        return mypageService.getView(userDetails.getMember(), page, size);
    }

    //찜한 게시글 보여주기
    @GetMapping("/mypage/favorite")
    public Page<PostResponseDto> getSave(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestParam(value = "page") int  page,
                                         @RequestParam(value = "size") int  size){
        return mypageService.getSave(userDetails.getMember(), page, size);
    }

    //사용자 보여주기
    @GetMapping("/review")
    public Page<ReviewResponseDto> getReview (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestParam(value = "page") int page,
                                              @RequestParam(value = "size") int size){
        return mypageService.getReview(userDetails.getMember(), page, size);
    }

}

/*
*           정의를 하면
*           이미지 정렬
*           이미지가 review 정렬
* */
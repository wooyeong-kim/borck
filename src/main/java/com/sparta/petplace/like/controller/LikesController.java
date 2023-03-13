package com.sparta.petplace.like.controller;

import com.sparta.petplace.auth.security.UserDetailsImpl;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.like.dto.LikesResponseDto;
import com.sparta.petplace.like.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{post_id}")
public class LikesController {
    private LikesService likesService;

    @PostMapping("/likes")
    public ApiResponseDto<LikesResponseDto> likes(@PathVariable Long post_id,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
    return likesService.likes(post_id,userDetails);
    }

    @DeleteMapping("/cancel")
    public ApiResponseDto<LikesResponseDto> cancel(@PathVariable Long post_id,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likesService.cancel(post_id,userDetails);
    }

}

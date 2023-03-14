package com.sparta.petplace.post.controller;


import com.sparta.petplace.auth.security.UserDetailsImpl;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.post.RequestDto.PostRequestDto;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import com.sparta.petplace.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

   private final PostService postService;

   @GetMapping("/posts")
   public List<PostResponseDto> getPosts(@RequestParam(value = "keyword") String keyword){
      return postService.getPosts(keyword);
   }

   @PostMapping("/post")
   public ApiResponseDto<PostResponseDto> createPost(@ModelAttribute PostRequestDto requestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
      return postService.createPost(requestDto,userDetails.getMember());
   }

   @GetMapping("/posts/{post_id}")
   private PostResponseDto getPostId(@PathVariable Long post_id ,@AuthenticationPrincipal UserDetailsImpl userDetails){
      return postService.getPostId(post_id,userDetails.getMember());
   }


}

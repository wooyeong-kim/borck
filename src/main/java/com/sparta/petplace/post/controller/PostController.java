package com.sparta.petplace.post.controller;

import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import com.sparta.petplace.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public List<PostResponseDto> getPosts(@RequestParam(value = "category") String category, Model model){
        List<PostResponseDto> postResponseDtoList = postService.getPosts(category);
        model.addAttribute("postList", postResponseDtoList);

        return postService.getPosts(category);
    }
}

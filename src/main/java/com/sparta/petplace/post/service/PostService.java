package com.sparta.petplace.post.service;


import com.sparta.petplace.S3Service;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ResponseUtils;
import com.sparta.petplace.exception.CustomException;
import com.sparta.petplace.exception.enumclass.Error;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.post.RequestDto.PostRequestDto;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import com.sparta.petplace.post.entity.Post;
import com.sparta.petplace.post.entity.PostImage;
import com.sparta.petplace.post.repository.PostImageRepository;
import com.sparta.petplace.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final S3Service s3Service;


    @Transactional
    public ApiResponseDto<PostResponseDto> createPost(PostRequestDto requestDto, Member member) {
        if(requestDto.getImages().isEmpty()){
            throw new CustomException(Error.WRONG_INPUT_CONTEN);
        }
        Post posts = new Post(requestDto.getEmail(),requestDto.getTitle(),requestDto.getCategory(),
                requestDto.getContents(),requestDto.getMapdate(),
                requestDto.getAddress(),requestDto.getTelNum(),requestDto.getCeo(),requestDto.getStartTime(),
                requestDto.getEndTime(),requestDto.getClosedDay(),member);
        postRepository.save(posts);
        List<String> imgList = new ArrayList<>();
        List<String> img_url = s3Service.upload(requestDto.getImages());
        for (String image: img_url) {
            PostImage img = new PostImage(posts,image);
            postImageRepository.save(img);
            imgList.add(image);
        }
        return ResponseUtils.ok(PostResponseDto.from(posts, imgList));

    }

}

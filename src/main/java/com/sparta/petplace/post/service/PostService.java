package com.sparta.petplace.post.service;


import com.sparta.petplace.S3Service;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ResponseUtils;
import com.sparta.petplace.exception.CustomException;
import com.sparta.petplace.exception.enumclass.Error;
import com.sparta.petplace.member.entity.LoginType;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;


    public List<PostResponseDto> getPosts(String keyword) {
        List<Post> posts = postRepository.findAByCategory(keyword);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for (Post p : posts){
//            p.getReviews().sort(Comparator.comparing(Review::gerCreatedAt).reversed());
//            List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
//            for(Review r : p.getReviews()){
//                reviewResponseDtos.add(new ReviewResponseDtoa(r));
//            }
            List<String> images = new ArrayList<>();
            for (PostImage postImage : p.getImages()) {
                images.add(postImage.getImage());
            }
            postResponseDtos.add(PostResponseDto.builder()
                    .post(p)
                    .image(images)
                    .build());
        }
        return  postResponseDtos;
    }

    @Transactional
    public ApiResponseDto<PostResponseDto> createPost(PostRequestDto requestDto, Member member) {
       Member member1 = memberRepository.findByLoginType(member.getLoginType());
       if(!member1.getLoginType().equals(LoginType.BUSINESS)) {
           throw new CustomException(Error.NO_AUTHORITY);
       }
        if(requestDto.getImage().isEmpty()){
            throw new CustomException(Error.WRONG_INPUT_CONTEN);
        }
        Post posts = Post.of(requestDto, member);
        postRepository.save(posts);
        List<String> imgList = new ArrayList<>();
        List<String> img_url = s3Service.upload(requestDto.getImage());
        for (String image: img_url) {
            PostImage img = new PostImage(posts,image);
            postImageRepository.save(img);
            imgList.add(image);
        }
        return ResponseUtils.ok(PostResponseDto.from(posts, imgList));

    }

}

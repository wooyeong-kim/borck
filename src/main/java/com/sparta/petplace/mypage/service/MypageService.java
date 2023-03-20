package com.sparta.petplace.mypage.service;

import com.sparta.petplace.S3Service;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ResponseUtils;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.exception.CustomException;
import com.sparta.petplace.exception.enumclass.Error;
import com.sparta.petplace.member.dto.MemberResponseDto;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.member.repository.MemberRepository;
import com.sparta.petplace.mypage.dto.MypageModifyRequestDto;
import com.sparta.petplace.mypage.entity.Mypage;
import com.sparta.petplace.mypage.repository.MypageRepository;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import com.sparta.petplace.post.entity.Post;
import com.sparta.petplace.post.repository.PostRepository;
import com.sparta.petplace.review.dto.ReviewResponseDto;
import com.sparta.petplace.review.entity.Review;
import com.sparta.petplace.review.repository.ReviewRepository;
import com.sparta.petplace.review.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageService {
    private final MypageRepository mypageRepository;
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;


    //사업자 마이페이지 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getView(Member member) {
        Optional<List<Post>> found = postRepository.findAllByEmail(member.getEmail());
        if (found.isEmpty()) {
            throw new CustomException(Error.NOT_FOUND_POST);
        }
        List<PostResponseDto> responseDtos = new ArrayList<>();
        for (Post post : found.get()) responseDtos.add(PostResponseDto.of(post));
        return responseDtos;
    }


    //찜한거 보여주기
    @Transactional(readOnly = true)
    public List<PostResponseDto> getSave(Member member) {
        List<Mypage> mypageList = mypageRepository.findByMemberId(member.getId());
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        for (Mypage mypage : mypageList)
            responseDtoList.add(PostResponseDto.of(mypage.getPost()));
        return responseDtoList;
    }


    //유저정보 수정
    @Transactional
    public ApiResponseDto<SuccessResponse> modify(MypageModifyRequestDto requestDto, Member member) {
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember.isEmpty()) {
            throw new CustomException(Error.NOT_EXIST_USER);
        }
        if (requestDto.getImage() == null || requestDto.getImage().isEmpty()) {
            findMember.get().update(requestDto.getNickname());
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "수정완료"));
        }
        s3Service.deleteFile(findMember.get().getImage());
        String image = null;
        try {
            image = s3Uploader.upload(requestDto.getImage(), requestDto.getImage().getOriginalFilename());
        } catch (IOException e) {
            throw new CustomException(Error.FAIL_S3_SAVE);
        }
        findMember.get().update(requestDto.getNickname(), image);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "수정완료"));
    }


    //유저 정보
    public ApiResponseDto<MemberResponseDto> getMember(Member member) {
        Optional<Member> found = memberRepository.findByEmail(member.getEmail());
        if (found.isEmpty()) {
            throw new CustomException(Error.NOT_EXIST_USER);
        }
        MemberResponseDto responseDto = MemberResponseDto.from(member);
        return ResponseUtils.ok(responseDto);
    }


    //사용자 정보
    public List<ReviewResponseDto> getReview(Member member, String cases) {
        List<Review> review = reviewRepository.findAllByMemberIdOrderByCreatedAtDesc(member.getId());
        List<ReviewResponseDto> responseDtoList = new ArrayList<>();
        boolean filter = Boolean.parseBoolean(cases);
        if (review == null) {
            throw new CustomException(Error.NOT_FOUND_POST);
        }
            for (Review r : review) {
                if(filter && r.getReview() != null){
                    ReviewResponseDto responseDto = new ReviewResponseDto(r);
                    responseDtoList.add(responseDto);
                }else if(!filter && r.getReview() == null){
                    ReviewResponseDto responseDto = new ReviewResponseDto(r);
                    responseDtoList.add(responseDto);
                }
        }
        return responseDtoList;
    }


}

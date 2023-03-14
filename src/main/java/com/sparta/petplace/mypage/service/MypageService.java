package com.sparta.petplace.mypage.service;

import com.sparta.petplace.S3Service;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ResponseUtils;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.exception.CustomException;
import com.sparta.petplace.exception.enumclass.Error;
import com.sparta.petplace.like.dto.LikesResponseDto;
import com.sparta.petplace.like.entity.Likes;
import com.sparta.petplace.like.repository.LikesRepository;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.member.repository.MemberRepository;
import com.sparta.petplace.mypage.dto.MypageModifyRequestDto;
import com.sparta.petplace.mypage.entity.Mypage;
import com.sparta.petplace.mypage.repository.MypageRepository;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import com.sparta.petplace.post.entity.Post;
import com.sparta.petplace.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageService {
    private final MypageRepository mypageRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;


//      사업자 마이페이지 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getView(Member member) {
        Optional<Post> found = postRepository.findByEmail(member.getEmail());
        if(found.isEmpty()){
            throw new CustomException(Error.NOT_FOUND_POST);
        }
        List<PostResponseDto> responseDtos = getPostResponseDtos(member);
        return responseDtos;
    }

//       게시물 보여주기 중복 메서드
    private List<PostResponseDto> getPostResponseDtos(Member member) {
        List<Mypage> mypageList = mypageRepository.findByMemberId(member.getId());
        List<PostResponseDto> responseDtos = new ArrayList<>();
        for(Mypage mypage : mypageList){
            responseDtos.add(PostResponseDto.of(mypage.getPost()));
        }
        return responseDtos;
    }

//    찜한거 보여주기
    @Transactional(readOnly = true)
    public List<PostResponseDto> getSave(Member member) {
        List<Mypage> mypageList = mypageRepository.findByMemberId(member.getId());
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        for(Mypage mypage:mypageList)
            responseDtoList.add(PostResponseDto.of(mypage.getPost()));
        return responseDtoList;
    }

//      사용자 마이페이지 조회



    @Transactional
    public ApiResponseDto<SuccessResponse> modify(MypageModifyRequestDto requestDto, Member member, Long user_id) {
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        String image = s3Service.uploadMypage(requestDto.getFile());
        findMember.get().update(requestDto.getNickname(), image);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "수정완료"));
    }
}

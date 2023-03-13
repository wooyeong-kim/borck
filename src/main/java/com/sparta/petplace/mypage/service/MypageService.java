package com.sparta.petplace.mypage.service;

import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.mypage.entity.Mypage;
import com.sparta.petplace.mypage.repository.MypageRepository;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import com.sparta.petplace.post.entity.Post;
import com.sparta.petplace.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final MypageRepository mypageRepository;
    private final PostRepository postRepository;


//      마이페이지 조회
    public List<PostResponseDto> getView(Member member) {
        List<Mypage> mypageList = mypageRepository.findByMemberId(member.getId());
        List<PostResponseDto> responseDtos = new ArrayList<>();
        for(Mypage mypage : mypageList){
            responseDtos.add(PostResponseDto.of(mypage.getPost()));
        }
        return responseDtos;
    }
}

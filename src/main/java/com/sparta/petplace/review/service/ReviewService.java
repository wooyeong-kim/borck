package com.sparta.petplace.review.service;

import com.sparta.petplace.S3Service;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ResponseUtils;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.post.entity.Post;
import com.sparta.petplace.post.repository.PostRepository;
import com.sparta.petplace.review.dto.ReviewRequestDto;
import com.sparta.petplace.review.dto.ReviewResponseDto;
import com.sparta.petplace.review.entity.Review;
import com.sparta.petplace.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;
    @Transactional
    public ApiResponseDto<ReviewResponseDto> createReview(Long post_id, ReviewRequestDto requestDto, Member member) {
        Post post = postRepository.findById(post_id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        String image = s3Service.uploadMypage(requestDto.getImage());
        Review review = reviewRepository.save(new Review(requestDto, image, post, member));
        return ResponseUtils.ok(ReviewResponseDto.from(review));
    }
}

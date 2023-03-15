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

import java.util.Optional;

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
        if (requestDto.getImage() == null || requestDto.getImage().isEmpty()) {
            Review review = reviewRepository.save(new Review(requestDto, null, post, member));
            return ResponseUtils.ok(ReviewResponseDto.from(review));
        }
        String image = s3Service.uploadMypage(requestDto.getImage());
        Review review = reviewRepository.save(new Review(requestDto, image, post, member));
        return ResponseUtils.ok(ReviewResponseDto.from(review));
    }

    @Transactional
    public ApiResponseDto<ReviewResponseDto> updateReview(Long review_id, ReviewRequestDto requestDto, Member member) {
        Review review = reviewRepository.findById(review_id).orElseThrow(
                () -> new IllegalArgumentException("해당 후기가 존재하지 않습니다.")
        );
        Optional<Review> exist = reviewRepository.findByIdAndMember(review_id, member);
        if (exist.isEmpty()) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        String image = s3Service.uploadMypage(requestDto.getImage());
        if (image == null || image.isEmpty()) {
            review.update(requestDto, image, member);
        }
        review.update(requestDto, image, member);
        return ResponseUtils.ok(ReviewResponseDto.from(review));
    }
}

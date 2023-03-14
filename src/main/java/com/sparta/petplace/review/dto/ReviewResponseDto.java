package com.sparta.petplace.review.dto;

import com.sparta.petplace.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String email;
    private Integer star;
    private String review;
    private String image;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public ReviewResponseDto(Review review, String image) {
        this.id = review.getId();
        this.email = review.getMember().getEmail();
        this.star = review.getStar();
        this.review = review.getReview();
        this.image = image;
        this.nickname = review.getMember().getNickname();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
    }

    public static ReviewResponseDto from(Review review) {
        return ReviewResponseDto.builder()
                .review(review)
                .build();
    }
}

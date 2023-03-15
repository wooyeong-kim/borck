package com.sparta.petplace.review.repository;

import com.sparta.petplace.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    void deleteByPostId(Long postId);

    Optional<List<Review>> findAllByMemberId(Long memberId);
}

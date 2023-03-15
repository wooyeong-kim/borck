package com.sparta.petplace.review.repository;

import com.sparta.petplace.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    void deleteByPostId(Long postId);
}

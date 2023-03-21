package com.sparta.petplace.review.repository;

import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    void deleteByPostId(Long postId);

    Optional<Review> findByIdAndMember(Long id, Member member);


    List<Review> findAllByMemberIdOrderByCreatedAtDesc(Long id);

    List<Review> findAllByMemberId(Long id);


//    @Modifying
//    @Query(value = "select  from Review order by image desc",nativeQuery = true)
//    List<Review> findAllByImage(String image);


//    List<Review> findAllByReview(Long id);
}

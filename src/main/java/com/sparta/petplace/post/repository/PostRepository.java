package com.sparta.petplace.post.repository;

import com.sparta.petplace.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAByCategory(String keyword);
    Optional<Post> findById(Long post_id);



    Optional<List<Post>> findAllByEmail(String email);


//    List<Post> findByCategoryAndKeywordContainingOrderByStarDesc(String category, String keyword);
//
//    List<Post> findByCategoryAndKeywordContainingOrderByDistanceDesc(String category, String keyword);
//
//    List<Post> findByCategoryAndKeywordContainingOrderByReviewDesc(String category, String keyword);
//
//    List<Post> findByCategoryAndKeywordContaining(String category, String keyword);
}

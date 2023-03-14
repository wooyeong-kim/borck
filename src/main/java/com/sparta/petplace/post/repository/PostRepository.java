package com.sparta.petplace.post.repository;

import com.sparta.petplace.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAByCategory(String keyword);
    Optional<Post> findById(Long post_id);

    Optional<Post> findByEmail(String email);
}

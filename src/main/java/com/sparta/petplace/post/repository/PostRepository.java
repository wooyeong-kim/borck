package com.sparta.petplace.post.repository;

import com.sparta.petplace.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

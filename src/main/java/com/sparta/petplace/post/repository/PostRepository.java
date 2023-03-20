package com.sparta.petplace.post.repository;

import com.sparta.petplace.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findByCategory(String category , Pageable pageable);
    List<Post> findByCategory(String category);
    Optional<List<Post>> findAllByEmail(String email);
    Optional<Post> findByTitle(String title);


}

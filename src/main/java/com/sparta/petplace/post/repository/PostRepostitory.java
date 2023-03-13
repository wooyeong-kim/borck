package com.sparta.petplace.post.repository;

import com.sparta.petplace.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepostitory extends JpaRepository<Post,Long> {
    List<Post> findAllByCategoryContaining(String categry);
}

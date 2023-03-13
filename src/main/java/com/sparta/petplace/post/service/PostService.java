package com.sparta.petplace.post.service;

import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import com.sparta.petplace.post.entity.Post;
import com.sparta.petplace.post.entity.PostImage;
import com.sparta.petplace.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepostitory;

    public List<PostResponseDto> getPosts(String categry) {
        List<Post> posts = postRepostitory.findAllByCategoryContaining(categry);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for (Post p : posts){
//            p.getReviews().sort(Comparator.comparing(Review::gerCreatedAt).reversed());
//            List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
//            for(Review r : p.getReviews()){
//                reviewResponseDtos.add(new ReviewResponseDtoa(r));
//            }
            List<String> images = new ArrayList<>();
            for (PostImage postImage : p.getImages()) {
                images.add(postImage.getImage());
            }
            postResponseDtos.add(PostResponseDto.builder()
                    .post(p)
                    .images(images)
                    .build());
        }
        return  postResponseDtos;
    }

}

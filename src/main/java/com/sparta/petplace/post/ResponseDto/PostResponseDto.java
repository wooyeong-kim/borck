package com.sparta.petplace.post.ResponseDto;

import com.sparta.petplace.post.entity.Post;
import com.sparta.petplace.review.dto.ReviewResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String email;
    private String title;
    private String ceo;
    private String category;
    private List<String> image;
    private String mapdata;
    private String address;
    private String telNum;
    private String startTime;
    private String endTime;
    private String closedDay;
    private List<ReviewResponseDto> review;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isSave;

    @Builder
    public PostResponseDto(Post post, List<String> image, boolean isSave, List<ReviewResponseDto> review){
        this.id = post.getId();
        this.email = post.getEmail();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.ceo = post.getCeo();
        this.image = image;
        this.mapdata = post.getMapdata();
        this.address = post.getAddress();
        this.telNum = post.getTelNum();
        this.startTime = post.getStartTime();
        this.endTime = post.getEndTime();
        this.closedDay = post.getClosedDay();
        this.review = review;
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.isSave = isSave;
    }

    public static PostResponseDto of(Post post){
        return PostResponseDto.builder()
                .post(post)
                .build();
    }
    public static PostResponseDto from(Post post,List<String> image){
        return PostResponseDto.builder()
                .post(post)
                .image(image)
                .build();
    }
    public static PostResponseDto of(Post post, List<String> image,List<ReviewResponseDto> responseDtoList,boolean isSave){
        return PostResponseDto.builder()
                .post(post)
                .image(image)
                .review(responseDtoList)
                .isSave(isSave)
                .build();
    }
}

package com.sparta.petplace.post.ResponseDto;

import com.sparta.petplace.member.entity.LoginType;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String email;
    private String title;
    private String ceo;
    private String category;
//    private List<ContentsResponseDto> contents;
    private List<String> image;
    private String mapdata;
    private String address;
    private Integer telNum;
    private Integer startTime;
    private Integer endTime;
    private Integer closedDay;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public PostResponseDto(Post post, List<String> image){// List<ContentsResponse> posts 추가
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
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
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
}

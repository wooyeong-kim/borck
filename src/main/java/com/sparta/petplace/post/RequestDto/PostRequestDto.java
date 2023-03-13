package com.sparta.petplace.post.RequestDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostRequestDto {
    private Long id;
    private String title;
    private String category;
    private String contents;
    private List<MultipartFile> images = new ArrayList<>();
    private Integer mapdate;
    private String address;
    private Integer telNum;
    private Integer startTime;
    private Integer endTime;
    private Integer closedDay;
}

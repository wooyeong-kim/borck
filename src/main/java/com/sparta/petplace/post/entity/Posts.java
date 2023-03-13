package com.sparta.petplace.post.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Posts {
//      업종 종류
    ANIMAL_HOSPITAL("병원"),       //  병원
    ANIMAL_BEAUTY("미용"),         //  미용
    ANIMAL_CAFE("카페?");            //  카페?


    private final String type;
}

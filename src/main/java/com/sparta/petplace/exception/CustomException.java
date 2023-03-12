package com.sparta.petplace.exception;

import com.sparta.petplace.exception.enumclass.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

    private final Error error;

}
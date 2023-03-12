package com.sparta.petplace.exception;

import com.sparta.petplace.exception.enumclass.Error;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ErrorResponse;
import com.sparta.petplace.common.ResponseUtils;
import org.springframework.http.HttpStatus;

public class GlobalExceptionHandling {

    public static ApiResponseDto<ErrorResponse> responseException(Error error) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(error.getStatus())
                .message(error.getMessage())
                .build();
        return ResponseUtils.error(errorResponse);
    }

    public static ApiResponseDto<ErrorResponse> responseException(HttpStatus status, String message) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.toString())
                .message(message)
                .build();
        return ResponseUtils.error(errorResponse);
    }
}

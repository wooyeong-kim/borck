package com.sparta.petplace.common;

public class ResponseUtils {

    public static <T> ApiResponseDto<T> ok(T data) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .response(data)
                .build();
    }

    public static <T> ApiResponseDto<T> error(ErrorResponse errorResponse) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .error(errorResponse)
                .build();
    }
}

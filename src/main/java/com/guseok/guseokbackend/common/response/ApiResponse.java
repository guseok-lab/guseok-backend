package com.guseok.guseokbackend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.guseok.guseokbackend.common.exception.ErrorCode;

public record ApiResponse<T>(
    @JsonProperty("success")
    boolean isSuccess,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    ErrorResponse error
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null,
            new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }


    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, null, new ErrorResponse(code, message));
    }
}

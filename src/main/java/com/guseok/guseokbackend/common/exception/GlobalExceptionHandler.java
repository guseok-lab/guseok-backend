package com.guseok.guseokbackend.common.exception;

import com.guseok.guseokbackend.common.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.error(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .orElse(ErrorCode.INVALID_REQUEST.getMessage());

        return ResponseEntity
            .status(ErrorCode.INVALID_REQUEST.getStatus())
            .body(ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity
            .status(ErrorCode.INVALID_REQUEST.getStatus())
            .body(ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParameterException(MissingServletRequestParameterException e) {
        String message = e.getParameterName() + " 값은 필수입니다.";
        return ResponseEntity
            .status(ErrorCode.INVALID_REQUEST.getStatus())
            .body(ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), message));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return ResponseEntity
            .status(ErrorCode.FILE_SIZE_EXCEEDED.getStatus())
            .body(ApiResponse.error(ErrorCode.FILE_SIZE_EXCEEDED));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity
            .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
            .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}

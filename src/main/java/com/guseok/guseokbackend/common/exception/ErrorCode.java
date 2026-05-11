package com.guseok.guseokbackend.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Common
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_001", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_999", "서버 오류가 발생했습니다."),

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "로그인이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "유효하지 않은 토큰입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_003", "접근 권한이 없습니다."),
    OAUTH_UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH_004", "지원하지 않는 OAuth 제공자입니다."),
    OAUTH_PROVIDER_ID_MISSING(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_005", "OAuth 제공자 ID를 찾을 수 없습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "사용자를 찾을 수 없습니다."),

    // Search
    SEARCH_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH_001", "탐색 요청을 찾을 수 없습니다."),
    SEARCH_ACCESS_DENIED(HttpStatus.FORBIDDEN, "SEARCH_002", "해당 탐색 요청에 접근할 수 없습니다."),
    INVALID_SEARCH_STATUS(HttpStatus.BAD_REQUEST, "SEARCH_003", "현재 상태에서는 처리할 수 없습니다."),

    // File
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_001", "파일 업로드에 실패했습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "FILE_002", "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "FILE_003", "파일 크기가 너무 큽니다."),

    // AI
    AI_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "AI_001", "AI 분석 요청에 실패했습니다."),
    AI_CALLBACK_INVALID(HttpStatus.BAD_REQUEST, "AI_002", "AI 콜백 요청이 올바르지 않습니다."),

    // Drone
    DRONE_CONNECT_FAILED(HttpStatus.BAD_GATEWAY, "DRONE_001", "드론 연결에 실패했습니다."),
    DRONE_NOT_FOUND(HttpStatus.NOT_FOUND, "DRONE_002", "드론을 찾을 수 없습니다."),
    DRONE_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "DRONE_003", "사용 가능한 드론이 아닙니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
package com.guseok.guseokbackend.entity;

public enum SearchStatus {
    REQUESTED,        // 탐색 요청됨
    UPLOAD_REQUESTED, // 업로드 URL 발급됨 (파일 업로드 대기)
    PENDING,          // 파일 업로드 완료, AI 분석 대기 중
    IN_PROGRESS,      // 탐색 진행 중
    COMPLETED,        // 탐색 완료
    FAILED,           // 탐색 실패
    CANCELED          // 탐색 취소
}

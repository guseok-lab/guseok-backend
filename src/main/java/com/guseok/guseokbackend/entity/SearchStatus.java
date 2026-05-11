package com.guseok.guseokbackend.entity;

public enum SearchStatus {
    REQUESTED,    // 탐색 요청됨
    IN_PROGRESS,  // 탐색 진행 중
    COMPLETED,    // 탐색 완료
    FAILED,       // 탐색 실패
    CANCELED      // 탐색 취소
}

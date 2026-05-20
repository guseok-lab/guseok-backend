package com.guseok.guseokbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "search_results")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 탐색 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_id", nullable = false)
    private Search search;

    // VIDEO, DRONE
    @Enumerated(EnumType.STRING)
    @Column(name = "result_type", nullable = false)
    private ResultType resultType;

    // FOUND, NOT_FOUND, FAILED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SearchResultStatus status;

    // 유사도 / 정확도
    private Double accuracy;

    // 탐지 이미지 URL
    @Column(name = "matched_image_url", length = 1000)
    private String matchedImageUrl;

    // 영상에서 발견된 시점 (초)
    @Column(name = "matched_time_seconds")
    private Integer matchedTimeSeconds;

    @Builder
    public SearchResult(
        Search search,
        ResultType resultType,
        SearchResultStatus status,
        Double accuracy,
        String matchedImageUrl,
        Integer matchedTimeSeconds
    ) {
        this.search = search;
        this.resultType = resultType;
        this.status = status;
        this.accuracy = accuracy;
        this.matchedImageUrl = matchedImageUrl;
        this.matchedTimeSeconds = matchedTimeSeconds;
    }
}

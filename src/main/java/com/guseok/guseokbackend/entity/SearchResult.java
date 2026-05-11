package com.guseok.guseokbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    // 탐지 영상 URL
    @Column(name = "matched_video_url", length = 1000)
    private String matchedVideoUrl;

    // 영상에서 발견된 시점
    @Column(name = "matched_time_seconds")
    private Integer matchedTimeSeconds;

    // 위도
    private Double latitude;

    // 경도
    private Double longitude;

    // 탐지 시각
    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @Builder
    public SearchResult(
        Search search,
        ResultType resultType,
        SearchResultStatus status,
        Double accuracy,
        String matchedImageUrl,
        String matchedVideoUrl,
        Integer matchedTimeSeconds,
        Double latitude,
        Double longitude,
        LocalDateTime detectedAt
    ) {
        this.search = search;
        this.resultType = resultType;
        this.status = status;
        this.accuracy = accuracy;
        this.matchedImageUrl = matchedImageUrl;
        this.matchedVideoUrl = matchedVideoUrl;
        this.matchedTimeSeconds = matchedTimeSeconds;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detectedAt = detectedAt;
    }
}

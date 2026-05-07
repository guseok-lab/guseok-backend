package com.guseok.guseokbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "search_videos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 탐색 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_id", nullable = false)
    private Search search;

    // 영상 URL
    @Column(name = "video_url", nullable = false, length = 1000)
    private String videoUrl;

    // 영상 길이
    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    // 파일 크기
    @Column(name = "file_size")
    private Long fileSize;

    // 생성일
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public SearchVideo(
        Search search,
        String videoUrl,
        Integer durationSeconds,
        Long fileSize
    ) {
        this.search = search;
        this.videoUrl = videoUrl;
        this.durationSeconds = durationSeconds;
        this.fileSize = fileSize;
    }
}
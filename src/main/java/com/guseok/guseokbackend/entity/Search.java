package com.guseok.guseokbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "searches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 요청 회원 (비회원이면 null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 성별
    @Column(nullable = false)
    private String gender;

    // 키
    @Column(nullable = false)
    private Integer height;

    // 몸무게
    @Column(nullable = false)
    private Integer weight;

    // 인상착의
    @Column(columnDefinition = "TEXT")
    private String appearance;

    // 기준 사진 URL
    @Column(name = "target_image_url", length = 1000)
    private String targetImageUrl;

    // VIDEO, DRONE
    @Enumerated(EnumType.STRING)
    @Column(name = "search_mode", nullable = false)
    private SearchMode searchMode;

    // 탐색 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SearchStatus status;

    @Builder
    public Search(
        Member member,
        String gender,
        Integer height,
        Integer weight,
        String appearance,
        String targetImageUrl,
        SearchMode searchMode,
        SearchStatus status
    ) {
        this.member = member;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.appearance = appearance;
        this.targetImageUrl = targetImageUrl;
        this.searchMode = searchMode;
        this.status = status;
    }

    public void updateStatus(SearchStatus status) {
        this.status = status;
    }
}

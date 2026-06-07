package com.guseok.guseokbackend.dto.search;

import com.guseok.guseokbackend.entity.Drone;
import com.guseok.guseokbackend.entity.Search;
import com.guseok.guseokbackend.entity.SearchMode;
import com.guseok.guseokbackend.entity.SearchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "탐색 상세 조회 응답")
public record SearchDetailResponse(

    @Schema(description = "탐색 ID", example = "1")
    Long searchId,

    @Schema(description = "성별", example = "남성")
    String gender,

    @Schema(description = "키 (cm)", example = "175")
    Integer height,

    @Schema(description = "몸무게 (kg)", example = "70")
    Integer weight,

    @Schema(description = "인상착의", example = "파란 재킷, 청바지 착용")
    String appearance,

    @Schema(description = "기준 사진 경로")
    String targetImageUrl,

    @Schema(description = "탐색 모드", example = "VIDEO")
    SearchMode searchMode,

    @Schema(description = "탐색 상태", example = "REQUESTED")
    SearchStatus status,

    @Schema(description = "첨부 영상 다운로드 URL 목록 (1시간 유효 presigned URL)")
    List<String> videoUrls,

    @Schema(description = "드론 고유 ID (searchMode=DRONE 일 때만 존재)", example = "drone-a1b2c3d4", nullable = true)
    String droneId,

    @Schema(description = "드론 MJPEG 스트림 URL (searchMode=DRONE 이고 드론 연결 완료 시 존재)", example = "https://xxx.trycloudflare.com/video/drone-a1b2c3d4", nullable = true)
    String streamUrl
) {
    public static SearchDetailResponse from(Search search, String targetImageUrl, List<String> videoUrls, Drone drone) {
        return new SearchDetailResponse(
            search.getId(),
            search.getGender(),
            search.getHeight(),
            search.getWeight(),
            search.getAppearance(),
            targetImageUrl,
            search.getSearchMode(),
            search.getStatus(),
            videoUrls,
            drone != null ? drone.getDroneId() : null,
            drone != null ? drone.getStreamUrl() : null
        );
    }
}
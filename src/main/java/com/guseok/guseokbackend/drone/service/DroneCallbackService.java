package com.guseok.guseokbackend.drone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.drone.DroneStreamHandler;
import com.guseok.guseokbackend.drone.dto.DroneDetectionRequest;
import com.guseok.guseokbackend.drone.dto.DroneStatusRequest;
import com.guseok.guseokbackend.drone.dto.DroneStatusResponse;
import com.guseok.guseokbackend.drone.dto.StreamUrlRequest;
import com.guseok.guseokbackend.drone.repository.DroneRepository;
import com.guseok.guseokbackend.entity.Drone;
import com.guseok.guseokbackend.entity.DroneStatus;
import com.guseok.guseokbackend.entity.ResultType;
import com.guseok.guseokbackend.entity.Search;
import com.guseok.guseokbackend.entity.SearchResult;
import com.guseok.guseokbackend.entity.SearchResultStatus;
import com.guseok.guseokbackend.repository.SearchRepository;
import com.guseok.guseokbackend.repository.SearchResultRepository;
import com.guseok.guseokbackend.service.AiRequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneCallbackService {

    private final DroneRepository droneRepository;
    private final SearchRepository searchRepository;
    private final SearchResultRepository searchResultRepository;
    private final DroneStreamHandler droneStreamHandler;
    private final AiRequestService aiRequestService;
    private final ObjectMapper objectMapper;

    @Transactional
    public DroneStatusResponse registerStream(StreamUrlRequest request) {
        Drone drone = droneRepository.findByDroneId(request.getDroneId())
            .orElseGet(() -> Drone.builder()
                .droneName(request.getDroneId())
                .status(DroneStatus.AVAILABLE)
                .build());

        if (Boolean.TRUE.equals(request.getConnected())) {
            drone.connectStream(request.getDroneId(), request.getStreamUrl(), request.getSearchId());
        } else {
            drone.disconnectStream();
        }

        droneRepository.save(drone);
        log.info("[드론] 스트림 등록 - droneId: {}, searchId: {}, connected: {}",
            request.getDroneId(), request.getSearchId(), request.getConnected());

        if (Boolean.TRUE.equals(request.getConnected()) && request.getSearchId() != null) {
            Search search = searchRepository.findById(request.getSearchId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SEARCH_NOT_FOUND));
            try {
                String presignedTargetImageUrl = search.getTargetImageUrl();
                aiRequestService.requestDroneAnalysis(
                    request.getSearchId(), request.getStreamUrl(), presignedTargetImageUrl);
            } catch (BusinessException e) {
                log.warn("[드론] AI 분석 요청 실패 - searchId: {}", request.getSearchId());
            }
        }

        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }

    @Transactional
    public DroneStatusResponse updateStatus(DroneStatusRequest request) {
        Drone drone = droneRepository.findByDroneId(request.getDroneId())
            .orElseThrow(() -> new BusinessException(ErrorCode.DRONE_NOT_FOUND));

        if (request.getStatus() == DroneStatus.DISCONNECTED) {
            drone.disconnectStream();
        }

        droneRepository.save(drone);
        log.info("[드론] 상태 업데이트 - droneId: {}, status: {}",
            request.getDroneId(), drone.getStatus());

        return new DroneStatusResponse(drone.getId(), drone.getStatus(), drone.getStreamUrl());
    }

    @Transactional
    public void handleDetection(DroneDetectionRequest request) {
        Search search = searchRepository.findById(request.searchId())
            .orElseThrow(() -> new BusinessException(ErrorCode.SEARCH_NOT_FOUND));

        SearchResult result = SearchResult.builder()
            .search(search)
            .resultType(ResultType.DRONE)
            .status(SearchResultStatus.valueOf(request.status()))
            .accuracy(request.accuracy())
            .matchedImageUrl(request.matchedImageUrl())
            .build();
        searchResultRepository.save(result);

        try {
            String resultJson = objectMapper.writeValueAsString(request);
            droneStreamHandler.sendDetectionResult(String.valueOf(request.searchId()), resultJson);
        } catch (Exception e) {
            log.error("[드론] WebSocket 전송 실패 - searchId: {}", request.searchId(), e);
        }

        log.info("[드론] 탐지 결과 처리 완료 - searchId: {}, status: {}", request.searchId(), request.status());
    }
}
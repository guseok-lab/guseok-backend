package com.guseok.guseokbackend.service;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiRequestService {

    private final RestClient restClient;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    @Value("${server.base-url}")
    private String serverBaseUrl;

    public AiRequestService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public void requestAnalysis(Long searchId, String videoUrl, String targetImageUrl) {
        try {
            var requestBody = new AiAnalysisRequest(
                searchId,
                videoUrl,
                targetImageUrl,
                serverBaseUrl + "/api/files/callback"
            );
            restClient.post()
                .uri(aiServerUrl + "/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.AI_REQUEST_FAILED);
        }
    }

    public void requestDroneAnalysis(Long searchId, String streamUrl, String targetImageUrl) {
        try {
            var requestBody = new AiDroneAnalysisRequest(
                searchId,
                streamUrl,
                targetImageUrl,
                serverBaseUrl + "/api/v1/drone-callback/detection"
            );
            restClient.post()
                .uri(aiServerUrl + "/analyze-stream")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.AI_REQUEST_FAILED);
        }
    }

    private record AiAnalysisRequest(
        Long searchId,
        String videoUrl,
        String targetImageUrl,
        String callbackUrl
    ) {}

    private record AiDroneAnalysisRequest(
        Long searchId,
        String streamUrl,
        String targetImageUrl,
        String callbackUrl
    ) {}
}
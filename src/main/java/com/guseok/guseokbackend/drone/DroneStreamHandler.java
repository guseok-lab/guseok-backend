package com.guseok.guseokbackend.drone;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DroneStreamHandler extends TextWebSocketHandler {

    // searchId별 연결된 프론트 세션 관리
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String searchId = extractSearchId(session);
        sessions.put(searchId, session);
        log.info("[WebSocket] 프론트 연결 - searchId: {}", searchId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 프론트에서 메시지 오면 로그만
        log.info("[WebSocket] 메시지 수신: {}", message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String searchId = extractSearchId(session);
        sessions.remove(searchId);
        log.info("[WebSocket] 프론트 연결 종료 - searchId: {}", searchId);
    }

    // AI 탐지 결과를 프론트로 브로드캐스트
    public void sendDetectionResult(String searchId, String resultJson) {
        WebSocketSession session = sessions.get(searchId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(resultJson));
                log.info("[WebSocket] 탐지 결과 전송 - searchId: {}", searchId);
            } catch (IOException e) {
                log.error("[WebSocket] 전송 실패 - searchId: {}", searchId, e);
            }
        }
    }

    // URL에서 searchId 추출
    // /ws/searches/1/drone → "1"
    private String extractSearchId(WebSocketSession session) {
        String path = session.getUri().getPath();
        String[] parts = path.split("/");
        return parts[3]; // searches/{searchId}/drone
    }
}
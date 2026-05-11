package com.guseok.guseokbackend.drone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

@Component
public class DroneStreamHandler extends BinaryWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(DroneStreamHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("드론 클라이언트 연결됨: {}", session.getId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        byte[] frameBytes = message.getPayload().array();
        log.info("프레임 수신 - 크기: {} bytes", frameBytes.length);
        // TODO: AI 서버로 전달
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("드론 클라이언트 연결 종료: {}", session.getId());
    }
}

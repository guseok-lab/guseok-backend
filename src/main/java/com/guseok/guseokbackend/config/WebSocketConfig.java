package com.guseok.guseokbackend.config;

import com.guseok.guseokbackend.drone.DroneStreamHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DroneStreamHandler droneStreamHandler;

    public WebSocketConfig(DroneStreamHandler droneStreamHandler) {
        this.droneStreamHandler = droneStreamHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(droneStreamHandler, "/ws/drone")
                .setAllowedOrigins("*");
    }
}

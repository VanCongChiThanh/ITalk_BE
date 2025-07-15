package com.chithanh.italk.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                // Cho phép connect và subscribe /topic/**
                .simpDestMatchers("/topic/**").authenticated()
                // Cho phép gửi tin nhắn đến /app/**
                .simpDestMatchers("/app/**").authenticated()
                // Cho phép kết nối CONNECT
                .simpTypeMatchers(org.springframework.messaging.simp.SimpMessageType.CONNECT).permitAll()
                .anyMessage().denyAll();
    }
    protected boolean sameOriginDisabled() {
        return true;
    }
}
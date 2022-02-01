package com.chat.backend.configs;

import com.chat.backend.AppHttpSessionHandshakeInterceptor;
import com.chat.backend.AppWebSocketHandler;
import com.chat.backend.AppWebSocketHandlerDecoratorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {
    @Value("#{'${cors.allowedOrigins}'}")
    private List<String> allowedOrigins;

    @Autowired
    private AppWebSocketHandler handler;

    @Autowired
    private AppWebSocketHandlerDecoratorFactory factory;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/sessions")
                .addInterceptors(new AppHttpSessionHandshakeInterceptor())
                .setAllowedOrigins(String.valueOf(allowedOrigins))
                .withSockJS();
    }

    /**
     * Add a factory to save web socket sessions to database
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(factory);

        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
    }
}

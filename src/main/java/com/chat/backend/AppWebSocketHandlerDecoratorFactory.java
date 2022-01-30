package com.chat.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Component
public class AppWebSocketHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    @Autowired
    private WebSocketSessionService webSocketSessionService;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {

            @Override
            public void afterConnectionEstablished(final org.springframework.web.socket.WebSocketSession session) throws Exception {
                // TODO save session details to mongo
                System.out.println("Web socket connection established.");
                super.afterConnectionEstablished(session);
                com.chat.backend.WebSocketSession ws = new com.chat.backend.WebSocketSession(session);
                try {
                    webSocketSessionService.create(ws);
                } catch (Exception e){
                    System.out.println("Unable to save web socket session to database.");
                }
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                // TODO update session details to mongo
                System.out.println("Web socket connection closed.");
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }
}

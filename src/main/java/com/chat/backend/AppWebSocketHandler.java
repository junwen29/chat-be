package com.chat.backend;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class AppWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String msg = String.format("Web socket session id=%s established. %s", session.getId(), session.getUri() );
        System.out.println(msg);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String msg = String.format("Web socket session id=%s disconnected. %s", session.getId(), session.getUri() );
        System.out.println(msg);
        super.afterConnectionClosed(session, status);
    }
}

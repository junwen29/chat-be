package com.chat.backend;

import com.chat.backend.services.WebSocketSessionService;
import com.chat.backend.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Component
@Log
public class AppWebSocketHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    @Autowired
    private WebSocketSessionService webSocketSessionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {

            @Override
            public void afterConnectionEstablished(final org.springframework.web.socket.WebSocketSession session) throws Exception {
                super.afterConnectionEstablished(session);
                com.chat.backend.entities.WebSocketSession ws = new com.chat.backend.entities.WebSocketSession(session);
                try {
                    String jwt = (String) session.getAttributes().get("jwt");
                    String id = jwtUtil.getUserId(jwt);
                    String email = jwtUtil.getEmail(jwt);
                    ws.setUserId(id);
                    ws.setName(email);
                    webSocketSessionService.create(ws);
                    String msg = String.format("Web socket connection with session id=%s established on user email=%s.", session.getId(), email);
                    log.info(msg);
                } catch (Exception e){
                    log.info("Unable to save web socket session to database.");
                }
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                // TODO update session details to mongo
                String jwt = (String) session.getAttributes().get("jwt");
                String email = jwtUtil.getEmail(jwt);
                String msg = String.format("Web socket connection with session id=%s closed on user email=%s.", session.getId(), email);
                log.info(msg);
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }
}

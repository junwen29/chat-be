package com.chat.backend;

import com.chat.backend.entities.ChatAppUser;
import com.chat.backend.repositories.UserRepository;
import com.chat.backend.services.WebSocketSessionService;
import com.chat.backend.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.util.Optional;

@Component
@Log
public class AppWebSocketHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    @Autowired
    private WebSocketSessionService webSocketSessionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {

            @Override
            public void afterConnectionEstablished(final org.springframework.web.socket.WebSocketSession session) throws Exception {
                super.afterConnectionEstablished(session);
                com.chat.backend.entities.WebSocketSession ws = new com.chat.backend.entities.WebSocketSession(session);
                try {
                    // JWT filter adds the jwt value to session attribute. We can retrieve it to identify the user.
                    String jwt = (String) session.getAttributes().get("jwt");
                    String id = jwtUtil.getUserId(jwt);

                    Optional<ChatAppUser> userOptional = userRepository.findById(id);
                    if (userOptional.isPresent()){
                        ws.setName(userOptional.get().getName());
                    }
                    else throw new RuntimeException(String.format("User not found with id= %s .", id));

                    ws.setUserId(id);
                    webSocketSessionService.create(ws);
                    String msg = String.format("Web socket connection with session id=%s established on user id=%s.", session.getId(), id);
                    log.info(msg);
                } catch (Exception e){
                    log.info("Unable to save web socket session to database.");
                }
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                // TODO update session details to mongo
                super.afterConnectionClosed(session, closeStatus);

                try {
                    // JWT filter adds the jwt value to session attribute. We can retrieve it to identify the user.
                    String jwt = (String) session.getAttributes().get("jwt");
                    String id = jwtUtil.getUserId(jwt);
                    String msg = String.format("Web socket connection with session id=%s closed on user id=%s.", session.getId(), id);
                    log.info(msg);

                    Optional<ChatAppUser> userOptional = userRepository.findById(id);
                    if (userOptional.isPresent()){
                        webSocketSessionService.closeSession(session.getId(), userOptional.get().getName());
                    }
                    else throw new RuntimeException(String.format("User not found with id= %s .", id));

                } catch (Exception e){
                    log.info("Unable to update web socket session to database.");
                }
            }
        };
    }
}

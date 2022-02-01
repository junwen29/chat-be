package com.chat.backend.services;

import com.chat.backend.entities.WebSocketSession;
import com.chat.backend.repositories.WebSocketSessionRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log
public class WebSocketSessionServiceImpl implements WebSocketSessionService {
    @Autowired
    private WebSocketSessionRepository webSocketSessionRepository;

    @Override
    public void create(WebSocketSession ws) {
        WebSocketSession saved = webSocketSessionRepository.save(ws);
        log.info(String.format("WebSocketSession details saved with session id = %s", saved.getSessionId()));
    }

    /**
     * @param webSocketSessionId id of the previous session created and use userId to uniquely identify the previous session.
     */
    @Override
    public void update(String webSocketSessionId) {

    }
}

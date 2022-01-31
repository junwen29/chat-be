package com.chat.backend.services;

import com.chat.backend.entities.WebSocketSession;
import com.chat.backend.repositories.WebSocketSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSocketSessionServiceImpl implements WebSocketSessionService {
    @Autowired
    private WebSocketSessionRepository webSocketSessionRepository;

    @Override
    public void create(WebSocketSession ws) {
        webSocketSessionRepository.save(ws);
    }
}

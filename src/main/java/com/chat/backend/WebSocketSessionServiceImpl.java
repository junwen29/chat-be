package com.chat.backend;

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

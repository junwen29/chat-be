package com.chat.backend.services;

import com.chat.backend.entities.WebSocketSession;

public interface WebSocketSessionService {

    void create(WebSocketSession ws);

    void update(String webSocketSessionId);
}

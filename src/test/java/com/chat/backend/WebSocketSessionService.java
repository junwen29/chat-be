package com.chat.backend;

import com.chat.backend.entities.WebSocketSession;

public interface WebSocketSessionService {

    void create(WebSocketSession ws);
}

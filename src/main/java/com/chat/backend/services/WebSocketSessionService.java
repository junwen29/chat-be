package com.chat.backend.services;

import com.chat.backend.entities.WebSocketSession;

import java.util.List;

public interface WebSocketSessionService {

    void create(WebSocketSession ws);

    void closeSession(String webSocketSessionId, String userName);

    List<WebSocketSession> findAllOpenSessions(String id);

    WebSocketSession getLastSession(String id);
}

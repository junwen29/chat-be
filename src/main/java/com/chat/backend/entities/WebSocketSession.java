package com.chat.backend.entities;

import com.chat.backend.enums.WebSocketSessionState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "web_socket_sessions")
public class WebSocketSession {

    @MongoId
    private String id;

    @Field("session_id")
    private String sessionId;

    private String uri;

    private String status;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    public WebSocketSession(org.springframework.web.socket.WebSocketSession ws) {
        this.sessionId = ws.getId();
        this.uri = Objects.requireNonNull(ws.getUri()).toString();
        this.status = ws.isOpen() ? String.valueOf(WebSocketSessionState.OPEN) : String.valueOf(WebSocketSessionState.CLOSED);
        this.createdAt = LocalDateTime.now();
    }
}

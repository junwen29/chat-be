package com.chat.backend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "web_socket_sessions")
public class WebSocketSession {

    @MongoId
    private String id;

    private String uri;

    private String status;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    public WebSocketSession(org.springframework.web.socket.WebSocketSession ws) {
        this.id = ws.getId();
        this.uri = ws.getUri().toString();
        this.status = ws.isOpen() ? "OPENED" : "NEW";
        this.created_at = LocalDateTime.now();
    }
}

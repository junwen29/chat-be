package com.chat.backend.entities;

import com.chat.backend.enums.WebSocketSessionState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "web_socket_sessions")
public class WebSocketSession extends BaseDocument {

    @Id
    private String id;

    @Field("session_id")
    private String sessionId;

    @Field("client_uri")
    private String clientUri;

    @Field("server_uri")
    private String serverUri;

    private String status;

    private String name;

    @Field("user_id")
    private String userId;

    public WebSocketSession(org.springframework.web.socket.WebSocketSession ws) {
        this.clientUri = ws.getHandshakeHeaders().getOrigin();
        this.sessionId = ws.getId();
        this.status = ws.isOpen() ? String.valueOf(WebSocketSessionState.OPEN) : String.valueOf(WebSocketSessionState.CLOSED);
    }
}

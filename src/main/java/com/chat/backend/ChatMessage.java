package com.chat.backend;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@Document(collection = "chat_messages")
public class ChatMessage {

    @MongoId
    private String id;

    private String text;

    private LocalDateTime created_at;

    private LocalDateTime modified_at;

    private String created_by;

    private String modified_by;
}

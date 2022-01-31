package com.chat.backend.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@Document(collection = "chat_messages")
public class ChatMessage {

    @MongoId
    private String id;

    private String text;

    @Field("session_id")
    private LocalDateTime createdAt;

    @Field("modified_at")
    private LocalDateTime modifiedAt;

    @Field("created_by")
    private String createdBy;

    @Field("modified_by")
    private String modifiedBy;
}

package com.chat.backend.entities;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

public class BaseDocument {

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}

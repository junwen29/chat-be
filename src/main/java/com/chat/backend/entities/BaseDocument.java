package com.chat.backend.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class BaseDocument {

    @Field("created_at")
    private String createdAt;

    @Field("updated_at")
    private String updatedAt;

    @Field("created_by")
    private String createdBy;

    @Field("updated_by")
    private String updatedBy;
}

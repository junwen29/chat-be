package com.chat.backend.dto;

import com.chat.backend.entities.BaseDocument;
import com.chat.backend.entities.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecryptedChatMessage extends BaseDocument {
    private String id;

    private String text;

    private String chatRoomId;

    public DecryptedChatMessage(ChatMessage encrypted) {
        this.id =encrypted.getId();
        this.chatRoomId = encrypted.getChatRoomId();

        setCreatedAt(encrypted.getCreatedAt());
        setCreatedBy(encrypted.getCreatedBy());

        setUpdatedAt(encrypted.getUpdatedAt());
        setUpdatedBy(encrypted.getUpdatedBy());
    }
}

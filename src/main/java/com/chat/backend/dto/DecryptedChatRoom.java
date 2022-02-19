package com.chat.backend.dto;

import com.chat.backend.entities.BaseDocument;
import com.chat.backend.entities.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecryptedChatRoom extends BaseDocument {
    private String id;

    private String avatar;

    private String title;

    private String lastMessage;

    private String lastMessageAt;

    public DecryptedChatRoom(ChatRoom chatRoom, String lastMessage) {
        this.id = chatRoom.getId();
        this.avatar = chatRoom.getAvatar();
        this.title = chatRoom.getTitle();
        this.lastMessage = lastMessage;
        this.lastMessageAt = chatRoom.getLastMessageAt();
    }
}
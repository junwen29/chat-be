package com.chat.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_rooms")
public class ChatRoom extends BaseDocument {
    @Id
    private String id;

    @Field("avatar")
    private String avatar;

    @Field("title")
    private String title;

    @Field("last_message")
    private String lastMessage;

    @Field("last_message_at")
    private String lastMessageAt;

    @Field("members")
    private List<ChatRoomMember> members;

    @Field("owner")
    private ChatRoomMember owner;
}

package com.chat.backend.entities;

import com.chat.backend.dto.SendMessageForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "chat_messages")
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends BaseDocument{

    @Id
    private String id;

    @Field("chat_room_id")
    private String chatRoomId;

    @Field("encrypted_text")
    private byte[] encryptedText;

    public ChatMessage(SendMessageForm form) {
        this.chatRoomId = form.getChatRoomId();
    }

    public String getInitials(){
        String[] arr = this.getCreatedBy().split(" ");
        if (arr.length > 2){
            char first = arr[0].toUpperCase().charAt(0);
            char last = arr[arr.length -1].toUpperCase().charAt(0);
            return String.valueOf(first) + last;
        }
        else {
            return String.valueOf(arr[0].toUpperCase().charAt(0));
        }
    }
}

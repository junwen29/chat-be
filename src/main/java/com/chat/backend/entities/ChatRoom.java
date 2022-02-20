package com.chat.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.stream.Collectors;

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
    private byte[] lastMessage;

    @Field("last_message_at")
    private String lastMessageAt;

    @Field("members")
    private List<ChatRoomMember> members;

    @Field("owner")
    private ChatRoomMember owner;

    /**
     * @param requesterId of the requester
     * @return based on the members name, the chat room with a title in consideration of the requester
     */
    public String resolveTitle(String requesterId){
        List<ChatRoomMember> members = this.members;
        members.removeIf(member -> member.getId().equals(requesterId));

        List<String> names = members.stream().map(ChatRoomMember::getName).collect(Collectors.toList());
        return names.get(0);
    }

    public String getInitials(){
        String[] arr = this.title.split(" ");
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

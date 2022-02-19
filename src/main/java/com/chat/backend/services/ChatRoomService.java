package com.chat.backend.services;

import com.chat.backend.dto.DecryptedChatRoom;
import com.chat.backend.entities.ChatMessage;
import com.chat.backend.entities.ChatRoom;
import com.chat.backend.entities.ChatRoomMember;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatRoomService {

    /**
     * @param id of the requester user
     * @return all the chat rooms the user participates in and last message is decrypted
     * if the chat room members size = 2, the title of the chat room should be the other member's name
     */
    List<DecryptedChatRoom> getUserChatRooms(String id);

    /**
     * @param userId of requester
     * @param selectedUserId of the user that requester selected
     * @return a new or existing chatroom both users are in
     */
    DecryptedChatRoom getChatRoomWithUser(String userId, String selectedUserId);

    /**
     * @param members with valid user ids
     * @return a new chat room with no title and the indicated members
     */
    ChatRoom create(List<ChatRoomMember> members, String avatar, ChatRoomMember owner);

    /**
     * @param lastMsg is the most recent chat message related to the chat room and saved to the db
     * update the last message and last updated by and last message at fields for additional sorting purpose
     */
    ChatRoom updateLastMessage(ChatMessage lastMsg);
}

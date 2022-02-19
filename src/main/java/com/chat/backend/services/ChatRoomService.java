package com.chat.backend.services;

import com.chat.backend.entities.ChatRoom;
import com.chat.backend.entities.ChatRoomMember;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatRoomService {

    /**
     * @param id of the requester user
     * @return all the chat rooms the user participates in
     * if the chat room members size = 2, the title of the chat room should be the other member's name
     */
    List<ChatRoom> getUserChatRooms(String id);

    /**
     * @param userId of
     * @param selectedUserId of
     * @return a new or existing chatroom both users are in
     */
    ChatRoom getChatRoomWithUser(String userId, String selectedUserId);

    /**
     * @param members with valid user ids
     * @return a new chat room with no title and the indicated members
     */
    ChatRoom create(List<ChatRoomMember> members, String avatar, ChatRoomMember owner);
}

package com.chat.backend.services;

import com.chat.backend.dto.DecryptedChatMessage;
import com.chat.backend.dto.MessageGroup;
import com.chat.backend.dto.SendMessageForm;
import com.chat.backend.entities.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    /**
     * @param form with chat room id and text
     * @param senderUserId person who submits the text
     * @return the saved messge
     * This method should
     * 1) save the chat message with encryption into DB
     * 2) update associated chat room as the last message
     * 3) broadcast a notification to online users in the chat room
     */
    ChatMessage send(SendMessageForm form, String senderUserId);

    DecryptedChatMessage get(String id);

    /**
     * @param chatRoomId of the chat room
     * @return a list of all the decrypted chat messages
     */
    List<MessageGroup> getChatRoomMessages(String chatRoomId, String requesterId);
}

package com.chat.backend.services;

import com.chat.backend.dto.DecryptedChatMessage;
import com.chat.backend.dto.SendMessageForm;
import com.chat.backend.entities.ChatMessage;

public interface ChatMessageService {
    ChatMessage save(SendMessageForm form, String senderUserId);

    DecryptedChatMessage get(String id);
}

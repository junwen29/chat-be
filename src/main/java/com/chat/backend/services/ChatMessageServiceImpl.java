package com.chat.backend.services;

import com.chat.backend.dto.DecryptedChatMessage;
import com.chat.backend.dto.SendMessageForm;
import com.chat.backend.entities.ChatMessage;
import com.chat.backend.repositories.ChatMessageRepository;
import com.chat.backend.utils.DateTimeUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log
public class ChatMessageServiceImpl implements ChatMessageService{

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Autowired
    private ChatAppUserService userService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private EncryptService encryptService;

    @Override
    public ChatMessage save(SendMessageForm form, String senderUserId) {
        ChatMessage message = new ChatMessage(form);
        String senderName = userService.getName(senderUserId);

        message.setCreatedAt(dateTimeUtil.now());
        message.setCreatedBy(senderName);

        message.setUpdatedAt(dateTimeUtil.now());
        message.setUpdatedBy(senderName);

        byte[] encrypted = encryptService.encrypt(form.getMessage());
        message.setEncryptedText(encrypted);

        ChatMessage saved = chatMessageRepository.save(message);
        String msg = String.format("%s saved a chat message.", senderName);
        log.info(msg);

        // TODO broadcast message saved event

        return saved;
    }

    @Override
    public DecryptedChatMessage get(String id) {
        ChatMessage encrypted = chatMessageRepository.findById(id).orElse(null);
        if (encrypted != null){
            DecryptedChatMessage decryptedChatMessage = new DecryptedChatMessage(encrypted);
            byte[] decryptedBytes = encryptService.decrypt(encrypted.getEncryptedText());
            String decrypted = new String(decryptedBytes);
            decryptedChatMessage.setText(decrypted);
            return decryptedChatMessage;
        }

        return null;
    }
}

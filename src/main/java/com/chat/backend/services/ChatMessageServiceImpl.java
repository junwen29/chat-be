package com.chat.backend.services;

import com.chat.backend.dto.DecryptedChatMessage;
import com.chat.backend.dto.MessageGroup;
import com.chat.backend.dto.SendMessageForm;
import com.chat.backend.entities.ChatMessage;
import com.chat.backend.entities.ChatRoom;
import com.chat.backend.repositories.ChatMessageRepository;
import com.chat.backend.utils.DateTimeUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MongoTemplate mongoTemplate;

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

        ChatRoom updated = chatRoomService.updateLastMessage(saved);

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
            decryptedChatMessage.setContent(decrypted);
            return decryptedChatMessage;
        }

        return null;
    }

    @Override
    public List<MessageGroup> getChatRoomMessages(String chatRoomId, String requesterId) {
        List<MessageGroup> messageGroups = new ArrayList<>();
        Query query = Query.query(Criteria.where("chat_room_id").is(chatRoomId));
        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class);

        // use Collectors.groupingBy with an adjusted LocalDate on the classifier, so that items with similar dates
        // are grouped together by days.
        Map<LocalDate, List<ChatMessage>> groupedByDates = messages.stream()
                .collect(Collectors.groupingBy(item -> {
                    try {
                        LocalDate date = dateTimeUtil.parseDate(item.getCreatedAt());
                        return date.with(
                                TemporalAdjusters.ofDateAdjuster(d -> d)
                        );
                    } catch (ParseException e) {
                        e.printStackTrace();
                        log.info(String.format("Unable to parse chat message created_at string: %s", item.getCreatedAt()));
                    }
                    return null;
                }));

        groupedByDates.forEach(((localDate, encryptedChatMessages) -> {
            MessageGroup mg = new MessageGroup();
            mg.setDate(dateTimeUtil.convertDateToStringDateOnly(localDate));
            mg.setLocalDate(localDate);

            List<DecryptedChatMessage> decryptedChatMessages = encryptedChatMessages.stream().map(m->{
                        DecryptedChatMessage d = new DecryptedChatMessage(m);
                        d.setContent(
                                new String(
                                        encryptService.decrypt(m.getEncryptedText())
                                )
                        );
                        String defaultAvatarUrl = "https://cdn.vuetifyjs.com/images/lists/3.jpg";
                        d.setAvatar(defaultAvatarUrl);

                        // use created_by to check if the message is from requester.
                        if (d.getCreatedBy().equals(userService.getName(requesterId))){
                            d.setFrom("Me");
                        }
                        else
                            d.setFrom(d.getCreatedBy());

                        // set to time only
                        try {
                            d.setCreatedAt(dateTimeUtil.getTimeOnly(d.getCreatedAt()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            log.info(String.format("Unable to parse created_at field: %s", d.getCreatedAt()));
                        }

                        return d;
                    })
                    .collect(Collectors.toList());

            mg.setMessages(decryptedChatMessages);

            messageGroups.add(mg);
        }));

        messageGroups.sort(new MessageGroup.MessageGroupComparator());
        return messageGroups;
    }
}

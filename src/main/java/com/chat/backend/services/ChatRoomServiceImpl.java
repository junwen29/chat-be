package com.chat.backend.services;

import com.chat.backend.dto.DecryptedChatRoom;
import com.chat.backend.entities.ChatMessage;
import com.chat.backend.entities.ChatRoom;
import com.chat.backend.entities.ChatRoomMember;
import com.chat.backend.repositories.ChatRoomRepository;
import com.chat.backend.utils.DateTimeUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log
public class ChatRoomServiceImpl implements ChatRoomService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Autowired
    private ChatRoomRepository repository;

    @Autowired
    private ChatAppUserService userService;

    @Autowired
    private EncryptService encryptService;

    @Override
    public List<DecryptedChatRoom> getUserChatRooms(String id) {
        Query query = new Query();
        query.addCriteria(
                Criteria.where("members.id").is(id)
        );
        query.with(Sort.by(Sort.Direction.DESC, "updated_at"));
        List<ChatRoom> chatRooms = mongoTemplate.find(query, ChatRoom.class);
        chatRooms.forEach(chatRoom -> {
            List<ChatRoomMember> members = chatRoom.getMembers();
            members.removeIf(member -> member.getId().equals(id));

            // Single Chat ==> need to add in the title
            if (members.size() == 1){
                List<String> names = members.stream().map(ChatRoomMember::getName).collect(Collectors.toList());
                chatRoom.setTitle(names.get(0));
            }
        });

        return chatRooms.stream().map(this::decryptChatRoom).collect(Collectors.toList());
    }

    @Override
    public DecryptedChatRoom getChatRoomWithUser(String userId, String selectedUserId) {
        Query query = new Query();

        query.addCriteria(
                new Criteria()
                        .andOperator(
                                Criteria.where("members.id").is(userId),
                                Criteria.where("members.id").is(selectedUserId)
                        )
        );
        query.with(Sort.by(Sort.Direction.DESC, "updated_at"));
        ChatRoom chatRoom = mongoTemplate.findOne(query, ChatRoom.class);

        if (chatRoom == null){
            List<ChatRoomMember> members = new ArrayList<>();
            ChatRoomMember owner = new ChatRoomMember(userId, userService.getName(userId));
            ChatRoomMember member1 = new ChatRoomMember(selectedUserId, userService.getName(selectedUserId));
            members.add(owner);
            members.add(member1);

            String defaultAvatarUrl = "https://cdn.vuetifyjs.com/images/lists/5.jpg";

            chatRoom = create(members, defaultAvatarUrl, owner);
        }

        return decryptChatRoom(chatRoom);
    }

    @Override
    public ChatRoom create(List<ChatRoomMember> members, String avatar, ChatRoomMember owner) {
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setAvatar(avatar);

        chatRoom.setCreatedBy(owner.getName());
        chatRoom.setCreatedAt(dateTimeUtil.now());

        chatRoom.setUpdatedBy(owner.getName());
        chatRoom.setUpdatedAt(dateTimeUtil.now());

        chatRoom.setMembers(members);
        chatRoom.setOwner(owner);
        String msg = String.format("%s created a chat room with %d members", owner.getName(), members.size());

        ChatRoom saved = repository.save(chatRoom);
        log.info(msg);

        return saved;
    }

    @Override
    public ChatRoom updateLastMessage(ChatMessage lastMsg) {
        ChatRoom chatRoom = repository.findById(lastMsg.getChatRoomId()).orElse(null);

        if (chatRoom != null){
            chatRoom.setLastMessage(lastMsg.getEncryptedText());
            chatRoom.setLastMessageAt(lastMsg.getCreatedAt());
            chatRoom.setUpdatedBy(lastMsg.getCreatedBy());
            return repository.save(chatRoom);
        }
        return null;
    }

    private DecryptedChatRoom decryptChatRoom(ChatRoom encrypted){
        byte[] encryptedLastMessage = encrypted.getLastMessage();
        byte[] decrypted = encryptService.decrypt(encryptedLastMessage);

        DecryptedChatRoom decryptedChatRoom = new DecryptedChatRoom(encrypted, new String(decrypted));
        try {
            String timeOnly = dateTimeUtil.getTimeOnly(decryptedChatRoom.getLastMessageAt());
            decryptedChatRoom.setLastMessageAt(timeOnly);
        } catch (ParseException e) {
            e.printStackTrace();
            log.info(String.format("Unable to parse chat room last message at field: %s", decryptedChatRoom.getLastMessageAt()));
        }
        return decryptedChatRoom;
    }
}

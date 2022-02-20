package com.chat.backend.controllers;

import com.chat.backend.dto.DecryptedChatRoom;
import com.chat.backend.dto.MessageGroup;
import com.chat.backend.services.ChatMessageService;
import com.chat.backend.services.ChatRoomService;
import com.chat.backend.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chats/rooms")
@Log
public class ChatRoomController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping
    public ResponseEntity<List<DecryptedChatRoom>> getChatRoomWithUser(
            @RequestParam(required = false, name = "userId") String userId,
            HttpServletRequest request
    ){
        String id = jwtUtil.getUserId(request);

        if (userId == null) {
            log.info(String.format("Request from user with id = %s to get chat rooms", id));
            List<DecryptedChatRoom> chatRooms = chatRoomService.getUserChatRooms(id);
            log.info(String.format("Returned %d chat rooms to user with id = %s", chatRooms.size(), id));
            return ResponseEntity.ok().body(chatRooms);
        }

        log.info(String.format("Request from user with id = %s to get chat rooms with user id = %s", id, userId));

        DecryptedChatRoom chatRoom = chatRoomService.getChatRoomWithUser(id, userId);
        log.info(String.format("Returned %d chat rooms to user with id = %s", 1, id));
        return ResponseEntity.ok().body(List.of(chatRoom));
    }

    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<List<MessageGroup>> getMessages(@PathVariable String chatRoomId, HttpServletRequest request){
        String id = jwtUtil.getUserId(request);
        List<MessageGroup> groupList = chatMessageService.getChatRoomMessages(chatRoomId, id);
        int count = groupList.stream().map( g -> g.getMessages().size()).reduce(0, Integer::sum);
        log.info(String.format("Returned %d chat messages to user with id = %s", count, id));

        return ResponseEntity.ok().body(groupList);
    }
}

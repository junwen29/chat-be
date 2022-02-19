package com.chat.backend.controllers;

import com.chat.backend.entities.ChatRoom;
import com.chat.backend.services.ChatRoomService;
import com.chat.backend.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public ResponseEntity<List<ChatRoom>> getChatRoomWithUser(
            @RequestParam(required = false, name = "userId") String userId,
            HttpServletRequest request
    ){
        String id = jwtUtil.getUserId(request);
        log.info(String.format("Request from user with id = %s to get chat rooms", id));

        if (userId == null) {
            List<ChatRoom> chatRooms = chatRoomService.getUserChatRooms(id);
            return ResponseEntity.ok().body(chatRooms);
        }

        ChatRoom chatRoom = chatRoomService.getChatRoomWithUser(id, userId);

        return ResponseEntity.ok().body(List.of(chatRoom));
    }
}

package com.chat.backend.controllers;

import com.chat.backend.dto.DecryptedChatMessage;
import com.chat.backend.dto.SendMessageForm;
import com.chat.backend.entities.ChatMessage;
import com.chat.backend.services.ChatMessageService;
import com.chat.backend.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/chats/messages")
@Log
public class ChatMessageController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<ChatMessage> sendMessage(@Valid @RequestBody SendMessageForm form, HttpServletRequest request){
        String id = jwtUtil.getUserId(request);
        log.info(String.format("POST request from user with id = %s to send message", id));
        ChatMessage saved = chatMessageService.send(form, id);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DecryptedChatMessage> get(@PathVariable String id){
        log.info(String.format("GET request from user with id = %s to get message", id));
        DecryptedChatMessage decryptedChatMessage = chatMessageService.get(id);

        if (decryptedChatMessage == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body(decryptedChatMessage);
    }
}

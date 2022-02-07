package com.chat.backend.controllers;

import com.chat.backend.dto.SendMessageForm;
import com.chat.backend.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/chats")
@Log
public class ChatController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public void sendMessage(@Valid @RequestBody SendMessageForm form){
        log.info(form.toString());
    }

    /**
     * This aims to return user the chat room with the selected user if any
     * @param userId belongs to the recipient that the user wants to chat with
     * @param request to get the user Id
     */
    @GetMapping("/rooms")
    public void getChatRoom(@RequestParam String userId, HttpServletRequest request){
        log.info(userId);
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = header.split(" ")[1].trim();
        String id = jwtUtil.getUserId(token);
    }
}

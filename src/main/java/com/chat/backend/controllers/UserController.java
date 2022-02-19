package com.chat.backend.controllers;

import com.chat.backend.dto.AccountRegistrationForm;
import com.chat.backend.dto.LoginForm;
import com.chat.backend.dto.LoginSuccessResponse;
import com.chat.backend.dto.UserListItem;
import com.chat.backend.services.ChatAppUserService;
import com.chat.backend.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Log
public class UserController {
    @Autowired
    private ChatAppUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public void register(@Valid @RequestBody AccountRegistrationForm form) {
        String msg = String.format("Received request with %s", form);
        log.info(msg);
        userService.register(form);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginSuccessResponse> authenticate(@Valid @RequestBody LoginForm form) {
        String msg = String.format("Received request with %s", form);
        log.info(msg);
        LoginSuccessResponse response = userService.login(form);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.getToken())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserListItem>> getAllUsers(HttpServletRequest request){
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = header.split(" ")[1].trim();
        String id = jwtUtil.getUserId(token);
        log.info(String.format("Request from user with id = %s to get all users", id));

        List<UserListItem> list = userService.getAll(id);

        return ResponseEntity.ok()
                .body(list);
    }
}
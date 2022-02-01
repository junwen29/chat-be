package com.chat.backend.controllers;

import com.chat.backend.dto.AccountRegistrationForm;
import com.chat.backend.dto.LoginForm;
import com.chat.backend.dto.LoginSuccessResponse;
import com.chat.backend.services.ChatAppUserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Log
@CrossOrigin
public class UserController {
    @Autowired
    private ChatAppUserService userService;

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
}
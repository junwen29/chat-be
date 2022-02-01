package com.chat.backend.controllers;

import com.chat.backend.dto.AccountRegistrationForm;
import com.chat.backend.dto.LoginForm;
import com.chat.backend.services.ChatAppUserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Log
@CrossOrigin
public class UserController {
    @Autowired
    private ChatAppUserService chatAppUserService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody AccountRegistrationForm form) {
        String msg = String.format("Received request with %s", form);
        log.info(msg);
        chatAppUserService.register(form);
    }

    @PostMapping("/authenticate")
    public void authenticate(@Valid @RequestBody LoginForm form) {
        String msg = String.format("Received request with %s", form);
        log.info(msg);
    }
}
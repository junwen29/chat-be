package com.chat.backend.controllers;

import com.chat.backend.configs.AdminFactory;
import com.chat.backend.dto.AdminKeyForm;
import com.chat.backend.services.EncryptService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/api/v1/admin")
@Log
public class AdminController {

    @Autowired
    private EncryptService encryptService;

    @PostMapping("/key")
    public ResponseEntity<SecretKey> getKey(@RequestBody AdminKeyForm form){
        try {
            SecretKey key = AdminFactory.getKeyFromPassword(form.getPassword(), form.getSalt());
            return ResponseEntity.ok().body(key);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            log.info("Unable to generate key");
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/salt")
    public ResponseEntity<String> getSalt(){
        String salt = AdminFactory.getNextSalt();
        return ResponseEntity.ok().body(salt);
    }

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody String content){
        byte[] encrypted = encryptService.encrypt(content);
        return ResponseEntity.ok().body(new String(encrypted));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestBody byte[] encrypted){
        byte[] decrypted = encryptService.decrypt(encrypted);
        return ResponseEntity.ok().body(new String(decrypted));
    }

}

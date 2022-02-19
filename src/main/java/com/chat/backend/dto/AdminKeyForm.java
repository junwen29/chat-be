package com.chat.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminKeyForm {
    private String password;
    private String salt;
}

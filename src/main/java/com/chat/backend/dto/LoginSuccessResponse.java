package com.chat.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessResponse {
    private String name;
    private String token;
    private String emailAddress;
    private String initials;
}

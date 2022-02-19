package com.chat.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageForm {

    @NotEmpty
    @NotNull
    private String message;

    @NotEmpty
    @NotNull
    private String chatRoomId;
}

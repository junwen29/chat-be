package com.chat.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListItem {
    private String id;
    private String avatarUrl;
    private String title;
    private String lastSeen;
    private boolean isOnline;
    private String initials;
}



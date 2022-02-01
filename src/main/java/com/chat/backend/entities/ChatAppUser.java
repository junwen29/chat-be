package com.chat.backend.entities;

import com.chat.backend.dto.AccountRegistrationForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class ChatAppUser extends BaseDocument{
    @MongoId
    private String id;

    private String name;

    private String email;

    public ChatAppUser(AccountRegistrationForm form) {
        this.name = form.getName();
        this.email = form.getEmail();
    }

    public ChatAppUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static ChatAppUser from(String name, String email) {
        return new ChatAppUser(name,email);
    }
}

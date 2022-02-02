package com.chat.backend.entities;

import com.chat.backend.dto.AccountRegistrationForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class ChatAppUser extends BaseDocument implements UserDetails, Serializable {
    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    @Field("is_account_non_expired")
    private boolean isAccountNonExpired;

    @Field("is_account_non_locked")
    private boolean isAccountNonLocked;

    @Field("is_credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Field("is_enabled")
    private boolean isEnabled;

    public ChatAppUser(AccountRegistrationForm form) {
        this.name = form.getName();
        this.email = form.getEmail();
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}

package com.chat.backend.services;

import com.chat.backend.dto.AccountRegistrationForm;

public interface ChatAppUserService {

    void register(AccountRegistrationForm form);
    void login();
    void logout();
}

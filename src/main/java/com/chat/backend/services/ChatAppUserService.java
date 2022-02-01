package com.chat.backend.services;

import com.chat.backend.dto.AccountRegistrationForm;
import com.chat.backend.dto.LoginForm;
import com.chat.backend.dto.LoginSuccessResponse;

public interface ChatAppUserService {

    void register(AccountRegistrationForm form);
    LoginSuccessResponse login(LoginForm form);
    void logout();
}

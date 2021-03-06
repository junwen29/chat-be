package com.chat.backend.services;

import com.chat.backend.dto.AccountRegistrationForm;
import com.chat.backend.dto.LoginForm;
import com.chat.backend.dto.LoginSuccessResponse;
import com.chat.backend.dto.UserListItem;
import com.chat.backend.entities.ChatAppUser;

import java.util.List;

public interface ChatAppUserService {

    void register(AccountRegistrationForm form);
    LoginSuccessResponse login(LoginForm form);
    void logout();

    /**
     * @param userId is the requester userId
     * @return a list of users without the requester user
     */
    List<UserListItem> getAll(String userId);

    /**
     * @param user for checking online status
     * @return true if user is online
     */
    boolean isUserOnline(ChatAppUser user);

    /**
     * @param user with a valid id
     * @return a string to describe various last seen statements up to days.
     *
     */
    String getLastSeen(ChatAppUser user);

    String getName(String userId);

    /**
     * @param userId of the requester
     * @return the avatar image url or default to something if not exists
     */
    String getAvatar(String userId);
}

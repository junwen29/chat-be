package com.chat.backend.services;

import com.chat.backend.dto.AccountRegistrationForm;
import com.chat.backend.dto.LoginForm;
import com.chat.backend.dto.LoginSuccessResponse;
import com.chat.backend.dto.UserListItem;
import com.chat.backend.entities.ChatAppUser;
import com.chat.backend.entities.WebSocketSession;
import com.chat.backend.exceptions.PasswordsDoNotMatchException;
import com.chat.backend.exceptions.UserAlreadyExistException;
import com.chat.backend.repositories.UserRepository;
import com.chat.backend.utils.DateTimeUtil;
import com.chat.backend.utils.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Log
public class ChatAppUserServiceImpl implements ChatAppUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Autowired
    private WebSocketSessionService webSocketSessionService;

    @Override
    public void register(AccountRegistrationForm form) {
        ChatAppUser user = new ChatAppUser(form);
        String name = form.getName();
        String email = form.getEmail();
        boolean isNameUnique = repository.findUsersByName(name).size() < 1;
        boolean isEmailUnique = repository.findUsersByEmail(email).size() < 1;
        boolean isPasswordMatch = form.getPassword().equals(form.getConfirmationPassword());

        if (!isNameUnique){
            throw new UserAlreadyExistException(String.format("There is an account with that name: %s.", name));
        }

        if (!isEmailUnique){
            throw new UserAlreadyExistException(String.format("There is an account with that email address: %s.", email));
        }

        if (!isPasswordMatch){
            throw new PasswordsDoNotMatchException("The passwords do not match.");
        }

        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setCreatedAt(dateTimeUtil.now());
        repository.save(user);
        log.info(String.format("New user registered: %s", user.getName()));
    }

    @Override
    public LoginSuccessResponse login(LoginForm form) {
        // Will hit BadCredentials Exceptions if credentials mismatched.
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword()));

        ChatAppUser user = (ChatAppUser) authenticate.getPrincipal();
        LoginSuccessResponse response = new LoginSuccessResponse();
        response.setToken(jwtUtil.generateAccessToken(user));
        response.setName(user.getName());
        response.setEmailAddress(user.getEmail());
        response.setInitials(user.getInitials());

        return response;
    }

    @Override
    public void logout() {
    // TODO close all chat web socket connections
    }

    /**
     * @param userId is the requester userId
     * @return a list of users without the requester user
     */
    @Override
    public List<UserListItem> getAll(String userId) {
        List<UserListItem> list = new ArrayList<>();

        List<ChatAppUser> chatAppUsers = repository.findAll();
        // remove requester user
        chatAppUsers.removeIf(chatAppUser -> chatAppUser.getId().equals(userId));

        for (int i = 0; i < chatAppUsers.size(); i++) {
            ChatAppUser u = chatAppUsers.get(i);
            list.add(
                    new UserListItem(
                            u.getId(),
                            String.format("https://cdn.vuetifyjs.com/images/lists/%d.jpg", i+1),
                            u.getName(),
                            getLastSeen(u),
                            isUserOnline(u)
                    )
            );
        }

        return list;
    }

    /**
     * @param u for checking online status
     * @return true if user is online
     */
    @Override
    public boolean isUserOnline(ChatAppUser u) {
        List<WebSocketSession> webSocketSessions = webSocketSessionService.findAllOpenSessions(u.getId());
        return webSocketSessions.size() > 0;
    }

    @Override
    public String getLastSeen(ChatAppUser u) {
        WebSocketSession ws = webSocketSessionService.getLastSession(u.getId());

        // user has never established web socket session before
        if (ws == null){
            return null;
        }
        else {
            String lastUpdatedTime = ws.getUpdatedAt();

            long diffInMillieSeconds;
            try {
                diffInMillieSeconds = dateTimeUtil.difference(lastUpdatedTime, dateTimeUtil.now());

                long diffMinutes = TimeUnit.MINUTES.convert(diffInMillieSeconds, TimeUnit.MILLISECONDS);

                if (diffMinutes < 2){
                    // we want to avoid returning // "last seen 1 minutes ago"
                    return "last seen a minute ago";
                }
                else if (diffMinutes < 60){
                    return String.format("last seen %d minutes ago", diffMinutes);
                }
                else {
                    long diffHours = TimeUnit.HOURS.convert(diffInMillieSeconds, TimeUnit.MILLISECONDS);
                    if (diffHours < 24){
                        return String.format("last seen %d hours ago", diffHours);
                    }
                    else {
                        long diffDays = TimeUnit.DAYS.convert(diffInMillieSeconds, TimeUnit.MILLISECONDS);
                        if (diffDays < 7) {
                            return String.format("last seen %d days ago", diffDays);
                        }
                        else {
                            return String.format("last seen on %s", dateTimeUtil.getDateOnly(lastUpdatedTime));
                        }
                    }
                }

            } catch (ParseException e) {
                log.severe(String.format("FAILED to parse last updated time: %s while checking for last seen of user: %s", lastUpdatedTime, u.getName()));
            }
        }

        return null;
    }
}

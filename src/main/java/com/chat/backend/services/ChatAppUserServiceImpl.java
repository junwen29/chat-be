package com.chat.backend.services;

import com.chat.backend.dto.AccountRegistrationForm;
import com.chat.backend.entities.ChatAppUser;
import com.chat.backend.exceptions.UserAlreadyExistException;
import com.chat.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChatAppUserServiceImpl implements ChatAppUserService {

    @Autowired
    private UserRepository repository;

    @Override
    public void register(AccountRegistrationForm form) {
        ChatAppUser user = new ChatAppUser(form);
        String name = form.getName();
        String email = form.getEmail();
        boolean isNameUnique = repository.findUsersByName(name).size() < 1;
        boolean isEmailUnique = repository.findUsersByEmail(email).size() < 1;

        if (!isNameUnique){
            throw new UserAlreadyExistException(String.format("There is an account with that name: %s.", name));
        }

        if (!isEmailUnique){
            throw new UserAlreadyExistException(String.format("There is an account with that email address: %s.", email));
        }

        repository.save(user);
    }

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }

}

package com.chat.backend.repositories;

import com.chat.backend.entities.ChatAppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<ChatAppUser, String> {
    List<ChatAppUser> findUsersByName(String name);

    List<ChatAppUser> findUsersByEmail(String email);

    Optional<ChatAppUser> findByName(String name);

    Optional<ChatAppUser> findByEmail(String email);
}

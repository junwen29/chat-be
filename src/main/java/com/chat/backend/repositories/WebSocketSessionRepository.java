package com.chat.backend.repositories;

import com.chat.backend.entities.WebSocketSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSocketSessionRepository extends MongoRepository<WebSocketSession, String> {

}

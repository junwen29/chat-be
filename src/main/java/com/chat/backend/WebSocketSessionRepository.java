package com.chat.backend;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSocketSessionRepository extends MongoRepository<WebSocketSession, String> {
}

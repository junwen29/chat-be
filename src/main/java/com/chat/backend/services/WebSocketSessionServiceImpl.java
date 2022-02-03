package com.chat.backend.services;

import com.chat.backend.entities.WebSocketSession;
import com.chat.backend.enums.WebSocketSessionState;
import com.chat.backend.repositories.WebSocketSessionRepository;
import com.chat.backend.utils.DateTimeUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Service
@Log
public class WebSocketSessionServiceImpl implements WebSocketSessionService {
    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private WebSocketSessionRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Override
    public void create(WebSocketSession ws) {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            ws.setClientUri(String.format("http://%s:%s", ip.getHostAddress(), serverPort));
        } catch (UnknownHostException e) {
            log.info("Unable to detect server ip address and host name to record in web socket session documents");
        }
        ws.setCreatedAt(dateTimeUtil.now());
        WebSocketSession saved = repository.save(ws);
        log.info(String.format("New WebSocketSession details saved with session id = %s", saved.getSessionId()));
    }

    /**
     * @param webSocketSessionId id of the previous session created and use user email to uniquely identify the previous session.
     */
    @Override
    public void closeSession(String webSocketSessionId, String userName) {
        WebSocketSession ws = mongoTemplate.findOne(
                Query.query(
                        Criteria.where("name").is(userName)
                                .and("session_id").is(webSocketSessionId)
                                .and("status").is(String.valueOf(WebSocketSessionState.OPEN))
                ), WebSocketSession.class);
        if (ws != null){
            ws.setStatus(String.valueOf(WebSocketSessionState.CLOSED));
            ws.setUpdatedAt(dateTimeUtil.now());
            repository.save(ws);
            log.info(String.format("Updated web socket session document from database base with name=%s and session id=%s .",userName, webSocketSessionId));
        }
        else {
            log.info(String.format("Unable to find web socket session document from database base with name=%s and session id=%s .",userName, webSocketSessionId));
        }

    }
}

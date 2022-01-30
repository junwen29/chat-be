package com.chat.backend;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatSender {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue send;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void sendChatMessage() {
        String message = "Hello World!";
        this.template.convertAndSend(send.getName(), message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}

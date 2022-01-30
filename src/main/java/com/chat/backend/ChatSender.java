package com.chat.backend;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatSender {
    @Autowired
    private RabbitTemplate template;

    @Value("${app.scheduledChatMessage}")
    private boolean shouldScheduledChatMessage;

    /**
     * When the user sends a chat message, it will publish to this queue.
     * This app will also consume messages from this queue and deliver the message to the correct user device.
     */
    @Autowired
    private Queue send;

//    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void sendChatMessage() {
        String message = "Hello World!";
        if (shouldScheduledChatMessage){
            this.template.convertAndSend(send.getName(), message);
            System.out.println(" [x] Sent '" + message + "'");
        }
        else
            System.out.println(" [x] Not sending scheduled message");
    }
}

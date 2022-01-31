package com.chat.backend;

import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "${rabbitmq.queues.sendChat}")
@Log
public class ChatReceiver {

    @RabbitHandler
    public void receive(String in) {
        log.info(" [x] Received '" + in + "'");
    }
}

package com.chat.backend.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Contains all the queue names
 */
@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queues.sendChat}")
    private String sendChatQueueName;

    @Bean
    public Queue send(){
        return new Queue(sendChatQueueName);
    }

}

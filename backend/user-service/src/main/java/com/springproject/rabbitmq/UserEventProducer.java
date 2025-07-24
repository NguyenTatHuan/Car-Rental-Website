package com.springproject.rabbitmq;

import com.springproject.dto.rabbitmq.UserCreatedEvent;
import com.springproject.dto.rabbitmq.UserDeletedEvent;
import com.springproject.dto.rabbitmq.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    public static final String EXCHANGE = "user.exchange";

    public static final String CREATED_ROUTING_KEY = "user.created";
    public static final String UPDATED_ROUTING_KEY = "user.updated";
    public static final String DELETED_ROUTING_KEY = "user.deleted";

    private final RabbitTemplate rabbitTemplate;

    public void sendUserCreatedEvent(UserCreatedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, CREATED_ROUTING_KEY, event);
    }

    public void sendUserUpdatedEvent(UserUpdatedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, UPDATED_ROUTING_KEY, event);
    }

    public void sendUserDeletedEvent(UserDeletedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, DELETED_ROUTING_KEY, event);
    }

}

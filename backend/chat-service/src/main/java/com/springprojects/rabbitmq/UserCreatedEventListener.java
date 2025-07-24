package com.springprojects.rabbitmq;

import com.springprojects.dto.rabbitmq.UserCreatedEvent;
import com.springprojects.dto.rabbitmq.UserDeletedEvent;
import com.springprojects.dto.rabbitmq.UserUpdatedEvent;
import com.springprojects.entity.Conversation;
import com.springprojects.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedEventListener {

    private final ConversationRepository conversationRepository;

    @RabbitListener(queues = "conversation.queue")
    public void handleUserCreated(UserCreatedEvent event) {
        Conversation conversation = Conversation.builder()
                .customerId(event.getUserId())
                .username(event.getUsername())
                .fullName(event.getFullName())
                .build();

        conversationRepository.save(conversation);
    }

    @RabbitListener(queues = "conversation.update.queue")
    public void handleUserUpdated(UserUpdatedEvent event) {
        Optional<Conversation> optionalConversation = conversationRepository.findByCustomerId(event.getUserId());

        optionalConversation.ifPresent(conversation -> {
            conversation.setFullName(event.getNewFullName());
            conversationRepository.save(conversation);
        });
    }

    @RabbitListener(queues = "conversation.delete.queue")
    public void handleUserDeleted(UserDeletedEvent event) {
        conversationRepository.deleteByCustomerId(event.getUserId());
    }

}

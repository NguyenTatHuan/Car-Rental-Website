package com.springprojects.rabbitmq;

import com.springprojects.dto.rabbitmq.UserCreatedEvent;
import com.springprojects.dto.rabbitmq.UserDeletedEvent;
import com.springprojects.dto.rabbitmq.UserUpdatedEvent;
import com.springprojects.elasticsearch.ConversationDocument;
import com.springprojects.entity.Conversation;
import com.springprojects.repository.ConversationElasticsearchRepository;
import com.springprojects.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedEventListener {

    private final ConversationRepository conversationRepository;

    private final ConversationElasticsearchRepository conversationElasticsearchRepository;

    private ConversationDocument mapToDocument(Conversation conversation) {
        return ConversationDocument.builder()
                .id(conversation.getId())
                .customerId(conversation.getCustomerId())
                .username(conversation.getUsername())
                .fullName(conversation.getFullName())
                .build();
    }

    @Transactional
    @RabbitListener(queues = "conversation.queue")
    public void handleUserCreated(UserCreatedEvent event) {
        Conversation conversation = Conversation.builder()
                .customerId(event.getUserId())
                .username(event.getUsername())
                .fullName(event.getFullName())
                .build();

        Conversation savedConversation = conversationRepository.save(conversation);

        try {
            conversationElasticsearchRepository.save(mapToDocument(savedConversation));
        } catch (Exception e) {
            log.error("Error in created conversation in ElasticSearch: {}", e.getMessage());
        }
    }

    @Transactional
    @RabbitListener(queues = "conversation.update.queue")
    public void handleUserUpdated(UserUpdatedEvent event) {
        Optional<Conversation> optionalConversation = conversationRepository.findByCustomerId(event.getUserId());

        optionalConversation.ifPresent(conversation -> {
            conversation.setFullName(event.getNewFullName());
            Conversation updatedConversation = conversationRepository.save(conversation);
            try {
                conversationElasticsearchRepository.save(mapToDocument(updatedConversation));
            } catch (Exception e) {
                log.error("Error in updated conversation in ElasticSearch: {}", e.getMessage());
            }
        });
    }

    @Transactional
    @RabbitListener(queues = "conversation.delete.queue")
    public void handleUserDeleted(UserDeletedEvent event) {
        conversationRepository.deleteByCustomerId(event.getUserId());
        try {
            conversationElasticsearchRepository.deleteByCustomerId(event.getUserId());
        } catch (Exception e) {
            log.error("Error in deleted conversation in ElasticSearch: {}", e.getMessage());
        }
    }

}

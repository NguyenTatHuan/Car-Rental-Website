package com.springprojects.service.conversation.customer;

import com.springprojects.dto.conversation.ConversationDto;
import com.springprojects.entity.Conversation;
import com.springprojects.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerConversationServiceImpl implements CustomerConversationService {

    private final ConversationRepository conversationRepository;

    private ConversationDto mapToDto(Conversation conversation) {
        return ConversationDto.builder()
                .conversationId(conversation.getId())
                .customerId(conversation.getCustomerId())
                .username(conversation.getUsername())
                .fullName(conversation.getFullName())
                .lastMessageAt(conversation.getLastMessageAt())
                .build();
    }

    @Override
    public ConversationDto getMyConversation(UUID customerId) {
        Conversation conversation = conversationRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found conversation with customerId:" + customerId));
        return mapToDto(conversation);
    }

}

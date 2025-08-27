package com.springprojects.service.conversation.admin;

import com.springprojects.dto.conversation.ConversationDto;
import com.springprojects.dto.conversation.CreatedConversationDto;
import com.springprojects.dto.conversation.UpdatedConversationDto;
import com.springprojects.elasticsearch.ConversationDocument;
import com.springprojects.entity.Conversation;
import com.springprojects.repository.ConversationElasticsearchRepository;
import com.springprojects.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminConversationServiceImpl implements AdminConversationService {

    private final ConversationRepository conversationRepository;

    private final ConversationElasticsearchRepository conversationElasticsearchRepository;

    private ConversationDto mapToDto(Conversation conversation) {
        return ConversationDto.builder()
                .conversationId(conversation.getId())
                .customerId(conversation.getCustomerId())
                .username(conversation.getUsername())
                .fullName(conversation.getFullName())
                .lastMessageAt(conversation.getLastMessageAt())
                .build();
    }

    private ConversationDocument mapToDocument(Conversation conversation) {
        return ConversationDocument.builder()
                .id(conversation.getId())
                .customerId(conversation.getCustomerId())
                .username(conversation.getUsername())
                .fullName(conversation.getFullName())
                .build();
    }

    @Override
    public ConversationDto createConversation(CreatedConversationDto createdDto) {
        Conversation conversation = Conversation.builder()
                .customerId(createdDto.getCustomerId())
                .username(createdDto.getUsername())
                .fullName(createdDto.getFullName())
                .build();

        Conversation savedConversation = conversationRepository.save(conversation);

        try {
            conversationElasticsearchRepository.save(mapToDocument(savedConversation));
        } catch (Exception e) {
            log.error("Error in created conversation in ElasticSearch: {}", e.getMessage());
        }

        return mapToDto(savedConversation);
    }

    @Override
    public ConversationDto updateConversation(UUID conversationId, UpdatedConversationDto updatedDto) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found conversation with id: " + conversationId));

        if (updatedDto.getCustomerId() != null) {
            conversation.setCustomerId(updatedDto.getCustomerId());
        }

        if (updatedDto.getUsername() != null && !updatedDto.getUsername().isBlank()) {
            conversation.setUsername(updatedDto.getUsername());
        }

        if (updatedDto.getFullName() != null && !updatedDto.getFullName().isBlank()) {
            conversation.setFullName(updatedDto.getFullName());
        }

        Conversation updatedConversation = conversationRepository.save(conversation);

        try {
            conversationElasticsearchRepository.save(mapToDocument(updatedConversation));
        } catch (Exception e) {
            log.error("Error in updated conversation in ElasticSearch: {}", e.getMessage());
        }

        return mapToDto(updatedConversation);
    }

    @Override
    public void deleteConversation(UUID conversationId) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found conversation with id: " + conversationId);
        }

        conversationRepository.deleteById(conversationId);

        try {
            conversationElasticsearchRepository.deleteById(conversationId);
        } catch (Exception e) {
            log.error("Error in deleted conversation in ElasticSearch: {}", e.getMessage());
        }
    }

    @Override
    public List<ConversationDto> getAllConversations() {
        List<Conversation> conversations = conversationRepository.findAll();
        return conversations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConversationDto getConversation(UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found conversation with id:" + conversationId));
        return mapToDto(conversation);
    }

}

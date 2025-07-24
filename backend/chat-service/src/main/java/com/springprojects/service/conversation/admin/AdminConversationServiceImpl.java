package com.springprojects.service.conversation.admin;

import com.springprojects.dto.conversation.ConversationDto;
import com.springprojects.dto.conversation.CreatedConversationDto;
import com.springprojects.dto.conversation.UpdatedConversationDto;
import com.springprojects.entity.Conversation;
import com.springprojects.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminConversationServiceImpl implements AdminConversationService {

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
    public ConversationDto createConversation(CreatedConversationDto createdDto) {
        Conversation conversation = Conversation.builder()
                .customerId(createdDto.getCustomerId())
                .username(createdDto.getUsername())
                .fullName(createdDto.getFullName())
                .build();

        return mapToDto(conversationRepository.save(conversation));
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

        return mapToDto(conversationRepository.save(conversation));
    }

    @Override
    public void deleteConversation(UUID conversationId) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found conversation with id: " + conversationId);
        }

        conversationRepository.deleteById(conversationId);
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

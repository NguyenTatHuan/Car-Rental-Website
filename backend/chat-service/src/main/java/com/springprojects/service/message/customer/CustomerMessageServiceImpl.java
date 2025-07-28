package com.springprojects.service.message.customer;

import com.springprojects.dto.message.MessageResponse;
import com.springprojects.dto.message.SendMessageRequest;
import com.springprojects.entity.Conversation;
import com.springprojects.entity.Message;
import com.springprojects.enums.MessageType;
import com.springprojects.enums.UserRole;
import com.springprojects.repository.ConversationRepository;
import com.springprojects.repository.MessageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerMessageServiceImpl implements CustomerMessageService {

    private final ConversationRepository conversationRepository;

    private final MessageRepository messageRepository;

    private MessageResponse mapToDto(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversationId())
                .sentAt(message.getSentAt())
                .senderId(message.getSenderId())
                .senderType(message.getSenderType())
                .content(message.getContent())
                .isSeen(message.isSeen())
                .messageType(message.getMessageType())
                .build();
    }

    private UUID getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Object userIdAttr = request.getAttribute("userId");
            if (userIdAttr instanceof UUID) {
                return (UUID) userIdAttr;
            } else if (userIdAttr instanceof String) {
                return UUID.fromString((String) userIdAttr);
            }
        }
        throw new IllegalStateException("Not found userId!");
    }

    @Override
    public MessageResponse sendMessage(SendMessageRequest request) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content must not be empty!");
        }

        UUID currentUserId = getCurrentUserId();

        Conversation conversation = conversationRepository.findByCustomerId(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found"));

        Message message = Message.builder()
                .id(UUID.randomUUID())
                .conversationId(conversation.getId())
                .senderId(currentUserId)
                .senderType(UserRole.CUSTOMER)
                .content(request.getContent())
                .sentAt(Instant.now())
                .isSeen(false)
                .messageType(request.getMessageType() != null ? request.getMessageType() : MessageType.TEXT)
                .build();

        Message savedMessage = messageRepository.save(message);
        conversation.setLastMessageAt(savedMessage.getSentAt());
        conversationRepository.save(conversation);

        return mapToDto(savedMessage);
    }

    @Override
    public List<MessageResponse> getMessagesByConversationId(UUID conversationId) {
        UUID currentUserId = getCurrentUserId();

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found!"));

        if (!conversation.getCustomerId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to view this conversation.");
        }

        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void markMessageAsSeen(UUID messageId) {
        UUID currentUserId = getCurrentUserId();

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found!"));

        if (message.getSenderId().equals(currentUserId) && message.getSenderType() == UserRole.CUSTOMER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot mark your own message as seen.");
        }

        Conversation conversation = conversationRepository.findById(message.getConversationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found"));

        if (!conversation.getCustomerId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to modify this message.");
        }

        if (!message.isSeen()) {
            message.setSeen(true);
            messageRepository.save(message);
        }

    }

}

package com.springprojects.service.message.customer;

import com.springprojects.dto.message.MessageResponse;
import com.springprojects.dto.message.SendMessageRequest;

import java.util.List;
import java.util.UUID;

public interface CustomerMessageService {

    MessageResponse sendMessage(SendMessageRequest request);

    List<MessageResponse> getMessagesByConversationId(UUID conversationId);

    void markMessageAsSeen(UUID messageId);

}

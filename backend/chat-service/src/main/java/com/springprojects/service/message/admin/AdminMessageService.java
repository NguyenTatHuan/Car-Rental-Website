package com.springprojects.service.message.admin;

import com.springprojects.dto.message.MessageResponse;
import com.springprojects.dto.message.SendMessageRequest;

import java.util.List;
import java.util.UUID;

public interface AdminMessageService {

    MessageResponse sendMessage(SendMessageRequest request);

    List<MessageResponse> getMessagesByConversationId(UUID conversationId);

    void markMessageAsSeen(UUID messageId);

}

package com.springprojects.service.message.customer;

import com.springprojects.dto.message.MessageResponse;
import com.springprojects.dto.message.SendMessageRequest;

import java.util.List;
import java.util.UUID;

public interface CustomerMessageService {

    MessageResponse sendMessage(UUID customerId, SendMessageRequest request);

    List<MessageResponse> getMessagesForCustomer(UUID customerId);

    void markMessageAsSeen(UUID customerId, UUID messageId);

}

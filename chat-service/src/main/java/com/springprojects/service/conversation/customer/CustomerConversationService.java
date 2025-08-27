package com.springprojects.service.conversation.customer;

import com.springprojects.dto.conversation.ConversationDto;

import java.util.UUID;

public interface CustomerConversationService {

    ConversationDto getMyConversation(UUID customerId);

}

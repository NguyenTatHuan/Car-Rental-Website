package com.springprojects.service.conversation.admin;

import com.springprojects.dto.conversation.ConversationDto;
import com.springprojects.dto.conversation.CreatedConversationDto;
import com.springprojects.dto.conversation.UpdatedConversationDto;

import java.util.List;
import java.util.UUID;

public interface AdminConversationService {

    ConversationDto createConversation(CreatedConversationDto createdDto);

    ConversationDto updateConversation(UUID conversationId, UpdatedConversationDto updatedDto);

    void deleteConversation(UUID conversationId);

    List<ConversationDto> getAllConversations();

    ConversationDto getConversation(UUID conversationId);

}

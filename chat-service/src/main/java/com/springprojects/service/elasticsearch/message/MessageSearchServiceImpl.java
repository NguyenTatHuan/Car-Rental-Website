package com.springprojects.service.elasticsearch.message;

import com.springprojects.elasticsearch.MessageDocument;
import com.springprojects.entity.Conversation;
import com.springprojects.repository.ConversationRepository;
import com.springprojects.repository.MessageElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageSearchServiceImpl implements MessageSearchService {

    private final ConversationRepository conversationRepository;

    private final MessageElasticsearchRepository messageElasticsearchRepository;

    @Override
    public List<MessageDocument> searchByKeywordAsCustomer(UUID customerId, String keyword) {
        Conversation conversation = conversationRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found!"));

        return messageElasticsearchRepository
                .findByConversationIdAndContentContaining(conversation.getId(), keyword);
    }

    @Override
    public List<MessageDocument> searchByKeywordAsAdmin(UUID conversationId, String keyword) {
        return messageElasticsearchRepository
                .findByConversationIdAndContentContaining(conversationId, keyword);
    }

}

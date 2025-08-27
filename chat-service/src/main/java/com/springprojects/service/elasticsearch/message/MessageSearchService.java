package com.springprojects.service.elasticsearch.message;

import com.springprojects.elasticsearch.MessageDocument;

import java.util.List;
import java.util.UUID;

public interface MessageSearchService {

    List<MessageDocument> searchByKeywordAsCustomer(UUID customerId, String keyword);

    List<MessageDocument> searchByKeywordAsAdmin(UUID conversationId, String keyword);

}

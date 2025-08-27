package com.springprojects.service.elasticsearch.conversation;

import com.springprojects.elasticsearch.ConversationDocument;

import java.util.List;

public interface ConversationSearchService {

    List<ConversationDocument> searchByKeyword(String keyword);

}

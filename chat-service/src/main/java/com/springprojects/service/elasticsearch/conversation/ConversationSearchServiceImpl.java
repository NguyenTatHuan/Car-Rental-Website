package com.springprojects.service.elasticsearch.conversation;

import com.springprojects.elasticsearch.ConversationDocument;
import com.springprojects.repository.ConversationElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ConversationSearchServiceImpl implements ConversationSearchService {

    private final ConversationElasticsearchRepository conversationElasticsearchRepository;

    @Override
    public List<ConversationDocument> searchByKeyword(String keyword) {
        Set<ConversationDocument> results = new HashSet<>();

        results.addAll(conversationElasticsearchRepository.findByUsernameContaining(keyword));
        results.addAll(conversationElasticsearchRepository.findByFullNameContaining(keyword));

        return new ArrayList<>(results);
    }

}

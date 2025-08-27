package com.springprojects.repository;

import com.springprojects.elasticsearch.MessageDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageElasticsearchRepository extends ElasticsearchRepository<MessageDocument, UUID> {

    List<MessageDocument> findByConversationIdAndContentContaining(UUID conversationId, String keyword);

}

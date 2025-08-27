package com.springprojects.repository;

import com.springprojects.elasticsearch.ConversationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationElasticsearchRepository extends ElasticsearchRepository<ConversationDocument, UUID> {

    List<ConversationDocument> findByUsernameContaining(String keyword);

    List<ConversationDocument> findByFullNameContaining(String keyword);

    void deleteByCustomerId(UUID customerID);

}

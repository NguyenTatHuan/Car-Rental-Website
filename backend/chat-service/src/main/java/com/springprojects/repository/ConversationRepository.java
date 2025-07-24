package com.springprojects.repository;

import com.springprojects.entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, UUID> {

    Optional<Conversation> findByCustomerId(UUID customerId);

    void deleteByCustomerId(UUID customerId);
}

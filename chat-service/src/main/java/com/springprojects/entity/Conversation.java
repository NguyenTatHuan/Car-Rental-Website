package com.springprojects.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conversations")
public class Conversation {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private UUID customerId;

    private String username;

    private String fullName;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant lastMessageAt = Instant.now();

}

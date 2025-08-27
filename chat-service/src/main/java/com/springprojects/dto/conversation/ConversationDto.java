package com.springprojects.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {

    private UUID conversationId;

    private UUID customerId;

    private String username;

    private String fullName;

    private Instant lastMessageAt;

}

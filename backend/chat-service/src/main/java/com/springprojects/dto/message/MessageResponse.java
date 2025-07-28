package com.springprojects.dto.message;

import com.springprojects.enums.MessageType;
import com.springprojects.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class MessageResponse {

    private UUID id;

    private UUID conversationId;

    private Instant sentAt;

    private UUID senderId;

    private UserRole senderType;

    private String content;

    private boolean isSeen;

    private MessageType messageType;

}

package com.springprojects.dto.message;

import com.springprojects.enums.MessageType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SendMessageRequest {

    private UUID conversationId;

    private String content;

    private MessageType messageType;

}

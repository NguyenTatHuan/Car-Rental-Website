package com.springprojects.entity;

import com.springprojects.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private UUID conversationId;

    private Instant sentAt;

    private String senderId;

    private String senderType;

    private String content;

    @Builder.Default
    private boolean isSeen = false;

    private MessageType messageType;

}

package com.springprojects.controller;

import com.springprojects.dto.message.MessageResponse;
import com.springprojects.dto.message.SendMessageRequest;
import com.springprojects.service.message.admin.AdminMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/message")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMessageController {

    private final AdminMessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody SendMessageRequest request) {
        MessageResponse sentMessage = messageService.sendMessage(request);
        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getMessagesByConversation(
            @PathVariable UUID conversationId) {
        List<MessageResponse> messages = messageService.getMessagesByConversationId(conversationId);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{messageId}/seen")
    public ResponseEntity<Void> markMessageAsSeen(@PathVariable UUID messageId) {
        messageService.markMessageAsSeen(messageId);
        return ResponseEntity.noContent().build();
    }

}

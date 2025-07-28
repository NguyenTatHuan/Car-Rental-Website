package com.springprojects.controller;

import com.springprojects.dto.message.SendMessageRequest;
import com.springprojects.dto.message.MessageResponse;
import com.springprojects.service.message.customer.CustomerMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/message")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerMessageController {

    private final CustomerMessageService customerMessageService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        MessageResponse sentMessage = customerMessageService.sendMessage(request);
        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getMessagesByConversationId(@PathVariable UUID conversationId) {
        List<MessageResponse> messages = customerMessageService
                .getMessagesByConversationId(conversationId);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{messageId}/seen")
    public ResponseEntity<Void> markMessageAsSeen(@PathVariable UUID messageId) {
        customerMessageService.markMessageAsSeen(messageId);
        return ResponseEntity.noContent().build();
    }

}

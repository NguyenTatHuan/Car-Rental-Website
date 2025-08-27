package com.springprojects.controller;

import com.springprojects.dto.message.SendMessageRequest;
import com.springprojects.dto.message.MessageResponse;
import com.springprojects.elasticsearch.MessageDocument;
import com.springprojects.service.elasticsearch.message.MessageSearchService;
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

    private final MessageSearchService messageSearchService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(
            @RequestAttribute("userId") UUID userId,
            @Valid @RequestBody SendMessageRequest request) {
        MessageResponse sentMessage = customerMessageService.sendMessage(userId, request);
        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<MessageResponse>> getMessagesForCustomer(@RequestAttribute("userId") UUID userId) {
        List<MessageResponse> messages = customerMessageService.getMessagesForCustomer(userId);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{messageId}/seen")
    public ResponseEntity<Void> markMessageAsSeen(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID messageId) {
        customerMessageService.markMessageAsSeen(userId, messageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MessageDocument>> searchMessages(
            @RequestAttribute("userId") UUID customerId,
            @RequestParam("keyword") String keyword) {
        List<MessageDocument> results = messageSearchService.searchByKeywordAsCustomer(customerId, keyword.trim());
        return ResponseEntity.ok(results);
    }

}

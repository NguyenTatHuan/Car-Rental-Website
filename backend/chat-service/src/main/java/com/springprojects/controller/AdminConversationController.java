package com.springprojects.controller;

import com.springprojects.dto.conversation.ConversationDto;
import com.springprojects.dto.conversation.CreatedConversationDto;
import com.springprojects.dto.conversation.UpdatedConversationDto;
import com.springprojects.service.conversation.admin.AdminConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/conversation")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminConversationController {

    private final AdminConversationService adminConversationService;

    @PostMapping
    public ResponseEntity<ConversationDto> createConversation(
            @Valid @RequestBody CreatedConversationDto createdDto
    ) {
        ConversationDto created = adminConversationService.createConversation(createdDto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConversationDto> updateConversation(
            @PathVariable("id") UUID conversationId,
            @Valid @RequestBody UpdatedConversationDto updatedDto
    ) {
        ConversationDto updated = adminConversationService.updateConversation(conversationId, updatedDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable("id") UUID conversationId) {
        adminConversationService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ConversationDto>> getAllConversations() {
        List<ConversationDto> conversations = adminConversationService.getAllConversations();
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> getConversationById(@PathVariable("id") UUID conversationId) {
        ConversationDto conversation = adminConversationService.getConversation(conversationId);
        return ResponseEntity.ok(conversation);
    }

}

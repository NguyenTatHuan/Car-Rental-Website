package com.springprojects.controller;

import com.springprojects.dto.conversation.ConversationDto;
import com.springprojects.service.conversation.customer.CustomerConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/conversation")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerConversationController {

    private final CustomerConversationService customerConversationService;

    @GetMapping("/{customerId}")
    public ResponseEntity<ConversationDto> getMyConversation(@RequestAttribute("userId") UUID customerId) {
        ConversationDto conversationDto = customerConversationService.getMyConversation(customerId);
        return ResponseEntity.ok(conversationDto);
    }

}

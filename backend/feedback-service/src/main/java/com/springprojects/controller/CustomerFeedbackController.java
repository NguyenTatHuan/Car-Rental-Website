package com.springprojects.controller;

import com.springprojects.dto.FeedbackCreateDto;
import com.springprojects.dto.FeedbackDto;
import com.springprojects.dto.FeedbackUpdateDto;
import com.springprojects.service.customer.feedback.CustomerFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/feedback")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerFeedbackController {

    private final CustomerFeedbackService customerFeedbackService;

    @PostMapping
    public ResponseEntity<FeedbackDto> createFeedback(@Valid @RequestBody FeedbackCreateDto dto) {
        FeedbackDto createdFeedback = customerFeedbackService.createFeedback(dto);
        return ResponseEntity.status(201).body(createdFeedback);
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDto> updateFeedback(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID feedbackId,
            @Valid @RequestBody FeedbackUpdateDto dto
    ) {
        FeedbackDto updatedFeedback = customerFeedbackService.updateFeedback(feedbackId, userId, dto);
        return ResponseEntity.ok(updatedFeedback);
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable UUID feedbackId, @RequestAttribute("userId") UUID userId) {
        customerFeedbackService.deleteFeedback(feedbackId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacksByUser(@RequestAttribute("userId") UUID userId) {
        List<FeedbackDto> feedbackList = customerFeedbackService.getAllFeedbacksByUser(userId);
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDto> getFeedbackById(
            @PathVariable UUID feedbackId,
            @RequestAttribute("userId") UUID userId
    ) {
        FeedbackDto feedback = customerFeedbackService.getFeedbackById(feedbackId, userId);
        return ResponseEntity.ok(feedback);
    }

}

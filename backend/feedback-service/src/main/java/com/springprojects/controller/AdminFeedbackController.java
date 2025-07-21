package com.springprojects.controller;

import com.springprojects.dto.FeedbackDto;
import com.springprojects.service.admin.feedback.AdminFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/feedback")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminFeedbackController {

    private final AdminFeedbackService adminFeedbackService;

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacks() {
        List<FeedbackDto> feedbackList = adminFeedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDto> getFeedbackById(@PathVariable UUID id) {
        FeedbackDto feedback = adminFeedbackService.getFeedbackById(id);
        return ResponseEntity.ok(feedback);
    }

}

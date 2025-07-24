package com.springprojects.service.customer.feedback;

import com.springprojects.dto.FeedbackCreateDto;
import com.springprojects.dto.FeedbackDto;
import com.springprojects.dto.FeedbackUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CustomerFeedbackService {

    FeedbackDto createFeedback(FeedbackCreateDto dto);

    FeedbackDto updateFeedback(UUID feedbackId, UUID userId, FeedbackUpdateDto dto);

    void deleteFeedback(UUID feedbackId, UUID userId);

    List<FeedbackDto> getAllFeedbacksByUser(UUID userId);

    FeedbackDto getFeedbackById(UUID feedbackId, UUID userId);

}

package com.springprojects.service.customer.feedback;

import com.springprojects.dto.FeedbackCreateDto;
import com.springprojects.dto.FeedbackDto;
import com.springprojects.dto.FeedbackUpdateDto;
import com.springprojects.entity.Feedback;
import com.springprojects.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerFeedbackServiceImpl implements CustomerFeedbackService {

    private final FeedbackRepository feedbackRepository;

    private FeedbackDto mapToDto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        dto.setId(feedback.getId());
        dto.setUserId(feedback.getUserId());
        dto.setBookingId(feedback.getBookingId());
        dto.setCarId(feedback.getCarId());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setCreatedAt(feedback.getCreatedAt());
        dto.setUpdatedAt(feedback.getUpdatedAt());
        return dto;
    }

    @Override
    public FeedbackDto createFeedback(FeedbackCreateDto dto) {
        Feedback feedback = new Feedback();

        feedback.setUserId(dto.getUserId());
        feedback.setBookingId(dto.getBookingId());
        feedback.setCarId(dto.getCarId());
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());

        return mapToDto(feedbackRepository.save(feedback));
    }

    @Override
    public FeedbackDto updateFeedback(UUID feedbackId, FeedbackUpdateDto dto) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found feedback with id:" + feedbackId));

        if (dto.getRating() != null) {
            feedback.setRating(dto.getRating());
        }

        if (dto.getComment() != null && !dto.getComment().isBlank()) {
            feedback.setComment(dto.getComment());
        }

        return mapToDto(feedbackRepository.save(feedback));
    }

    @Override
    public void deleteFeedback(UUID feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found feedback with id: " + feedbackId));
        feedbackRepository.delete(feedback);
    }

    @Override
    public List<FeedbackDto> getAllFeedbacksByUser(UUID userId) {
        return feedbackRepository.findAllByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public FeedbackDto getFeedbackById(UUID feedbackId, UUID userId) {
        Feedback feedback = feedbackRepository.findByIdAndUserId(feedbackId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found feedback with id: " + feedbackId + " for user: " + userId));
        return mapToDto(feedback);
    }

}

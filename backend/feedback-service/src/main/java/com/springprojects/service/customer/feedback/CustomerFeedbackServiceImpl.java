package com.springprojects.service.customer.feedback;

import com.springprojects.dto.FeedbackCreateDto;
import com.springprojects.dto.FeedbackDto;
import com.springprojects.dto.FeedbackUpdateDto;
import com.springprojects.entity.Feedback;
import com.springprojects.entity.RatingSummary;
import com.springprojects.repository.FeedbackRepository;
import com.springprojects.repository.RatingSummaryRepository;
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

    private final RatingSummaryRepository ratingSummaryRepository;

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

    private void updateRatingSummary(UUID carId) {
        List<Feedback> feedbacks = feedbackRepository.findAllByCarId(carId);

        double averageRating = feedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);

        int totalRatings = feedbacks.size();

        RatingSummary summary = ratingSummaryRepository.findById(carId)
                .orElseGet(() -> {
                    RatingSummary newSummary = new RatingSummary();
                    newSummary.setCarId(carId);
                    return newSummary;
                });

        summary.setAverageRating(averageRating);
        summary.setTotalRatings(totalRatings);

        ratingSummaryRepository.save(summary);
    }

    @Override
    public FeedbackDto createFeedback(FeedbackCreateDto dto) {
        Feedback feedback = new Feedback();

        feedback.setUserId(dto.getUserId());
        feedback.setBookingId(dto.getBookingId());
        feedback.setCarId(dto.getCarId());
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());

        Feedback saved = feedbackRepository.save(feedback);

        updateRatingSummary(saved.getCarId());

        return mapToDto(saved);
    }

    @Override
    public FeedbackDto updateFeedback(UUID feedbackId, UUID userId, FeedbackUpdateDto dto) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found feedback with id:" + feedbackId));

        if (!feedback.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this feedback!");
        }

        boolean shouldUpdateRatingSummary = false;

        if (dto.getRating() != null && !dto.getRating().equals(feedback.getRating())) {
            feedback.setRating(dto.getRating());
            shouldUpdateRatingSummary = true;
        }

        if (dto.getComment() != null && !dto.getComment().isBlank()) {
            feedback.setComment(dto.getComment());
        }

        Feedback saved = feedbackRepository.save(feedback);

        if (shouldUpdateRatingSummary) {
            updateRatingSummary(saved.getCarId());
        }

        return mapToDto(saved);
    }

    @Override
    public void deleteFeedback(UUID feedbackId, UUID userId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found feedback with id: " + feedbackId));

        if (!feedback.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this feedback!");
        }

        UUID carId = feedback.getCarId();

        feedbackRepository.delete(feedback);

        List<Feedback> remainingFeedbacks = feedbackRepository.findAllByCarId(carId);

        if (remainingFeedbacks.isEmpty()) {
            ratingSummaryRepository.deleteById(carId);
        } else {
            updateRatingSummary(carId);
        }
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

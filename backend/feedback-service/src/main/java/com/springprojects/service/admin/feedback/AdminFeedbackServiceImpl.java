package com.springprojects.service.admin.feedback;

import com.springprojects.dto.FeedbackDto;
import com.springprojects.entity.Feedback;
import com.springprojects.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminFeedbackServiceImpl implements AdminFeedbackService {

    private final FeedbackRepository feedbackRepository;

    private FeedbackDto mapToDto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        dto.setId(feedback.getId());
        dto.setBookingId(feedback.getBookingId());
        dto.setUserId(feedback.getUserId());
        dto.setCarId(feedback.getCarId());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setCreatedAt(feedback.getCreatedAt());
        dto.setUpdatedAt(feedback.getUpdatedAt());
        return dto;
    }

    @Override
    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackRepository.findAll().stream()
                .sorted(Comparator.comparing(Feedback::getCreatedAt).reversed())
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackDto getFeedbackById(UUID id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found feedback with ID: " + id));
        return mapToDto(feedback);
    }

}

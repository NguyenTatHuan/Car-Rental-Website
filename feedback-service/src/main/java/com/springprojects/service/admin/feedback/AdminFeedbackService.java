package com.springprojects.service.admin.feedback;

import com.springprojects.dto.FeedbackDto;

import java.util.List;
import java.util.UUID;

public interface AdminFeedbackService {

    List<FeedbackDto> getAllFeedbacks();

    FeedbackDto getFeedbackById(UUID id);

}

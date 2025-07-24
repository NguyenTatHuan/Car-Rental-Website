package com.springprojects.repository;

import com.springprojects.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    List<Feedback> findAllByUserId(UUID userId);

    Optional<Feedback> findByIdAndUserId(UUID feedbackId, UUID userId);

    List<Feedback> findAllByCarId(UUID carId);

}

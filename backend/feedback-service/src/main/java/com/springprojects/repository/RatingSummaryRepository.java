package com.springprojects.repository;

import com.springprojects.entity.RatingSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RatingSummaryRepository extends JpaRepository<RatingSummary, UUID> {
}

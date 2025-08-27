package com.springprojects.controller;

import com.springprojects.dto.RatingSummaryDto;
import com.springprojects.service.admin.ratingsummary.AdminRatingSummaryService;
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
@RequestMapping("/api/admin/rating-summary")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminRatingSummaryController {

    private final AdminRatingSummaryService ratingSummaryService;

    @GetMapping
    public ResponseEntity<List<RatingSummaryDto>> getAllSummaries() {
        return ResponseEntity.ok(ratingSummaryService.getAllSummaries());
    }

    @GetMapping("/{carId}")
    public ResponseEntity<RatingSummaryDto> getSummaryByCarId(@PathVariable UUID carId) {
        return ResponseEntity.ok(ratingSummaryService.getSummaryByCarId(carId));
    }

}

package com.springprojects.service.admin.ratingsummary;

import com.springprojects.dto.RatingSummaryDto;
import com.springprojects.entity.RatingSummary;
import com.springprojects.repository.RatingSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminRatingSummaryServiceImpl implements AdminRatingSummaryService {

    private final RatingSummaryRepository repository;

    private RatingSummaryDto mapToDto(RatingSummary summary) {
        RatingSummaryDto dto = new RatingSummaryDto();
        dto.setCarId(summary.getCarId());
        dto.setAverageRating(summary.getAverageRating());
        dto.setTotalRatings(summary.getTotalRatings());
        dto.setLastUpdated(summary.getLastUpdated());
        return dto;
    }

    @Override
    public List<RatingSummaryDto> getAllSummaries() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RatingSummaryDto getSummaryByCarId(UUID carId) {
        RatingSummary summary = repository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found rating summary for carId: " + carId));
        return mapToDto(summary);
    }

}

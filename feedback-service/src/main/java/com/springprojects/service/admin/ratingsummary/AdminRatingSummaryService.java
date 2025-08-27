package com.springprojects.service.admin.ratingsummary;

import com.springprojects.dto.RatingSummaryDto;

import java.util.List;
import java.util.UUID;

public interface AdminRatingSummaryService {

    List<RatingSummaryDto> getAllSummaries();

    RatingSummaryDto getSummaryByCarId(UUID carId);

}

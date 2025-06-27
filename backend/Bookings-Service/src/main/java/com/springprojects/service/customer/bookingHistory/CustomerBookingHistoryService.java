package com.springprojects.service.customer.bookingHistory;

import com.springprojects.dto.BookingHistoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface CustomerBookingHistoryService {

    List<BookingHistoryResponseDto> getBookingHistoriesByBookingId(UUID bookingId);

    List<BookingHistoryResponseDto> getBookingHistoriesByUser(UUID userId);

}

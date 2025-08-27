package com.springprojects.service.admin.bookingHistory;

import com.springprojects.dto.BookingHistoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminBookingHistoryService {

    List<BookingHistoryResponseDto> getAllHistory();

    List<BookingHistoryResponseDto> getHistoryByBookingId(UUID bookingId);

}

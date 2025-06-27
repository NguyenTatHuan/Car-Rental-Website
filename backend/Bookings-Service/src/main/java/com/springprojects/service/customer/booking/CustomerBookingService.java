package com.springprojects.service.customer.booking;

import com.springprojects.dto.BookingRequestDto;
import com.springprojects.dto.BookingResponseDto;

import java.util.List;
import java.util.UUID;

public interface CustomerBookingService {

    BookingResponseDto createBooking(UUID userId, BookingRequestDto dto);

    List<BookingResponseDto> getBookingsByUser(UUID userId);

    void cancelBooking(UUID userId, UUID bookingId);

}

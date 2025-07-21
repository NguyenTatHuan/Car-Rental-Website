package com.springprojects.service.customer.booking;

import com.springprojects.dto.booking.BookingRequestDto;
import com.springprojects.dto.booking.BookingResponseDto;
import com.springprojects.dto.booking.BookingUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CustomerBookingService {

    BookingResponseDto createBooking(UUID userId, BookingRequestDto dto);

    List<BookingResponseDto> getBookingsByUser(UUID userId);

    void cancelBooking(UUID userId, UUID bookingId);

    BookingResponseDto updateBooking(UUID userId, UUID bookingId, BookingUpdateDto dto);

}

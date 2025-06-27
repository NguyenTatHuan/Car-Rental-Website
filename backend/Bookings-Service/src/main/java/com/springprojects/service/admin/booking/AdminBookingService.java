package com.springprojects.service.admin.booking;

import com.springprojects.dto.BookingResponseDto;
import com.springprojects.enums.BookingStatus;

import java.util.List;
import java.util.UUID;

public interface AdminBookingService {

    List<BookingResponseDto> getAllBookings();

    BookingResponseDto getBookingById(UUID id);

    BookingResponseDto updateBookingStatus(UUID id, BookingStatus status);

    void deleteBooking(UUID id);

}

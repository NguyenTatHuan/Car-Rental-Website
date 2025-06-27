package com.springprojects.controller;

import com.springprojects.dto.BookingRequestDto;
import com.springprojects.dto.BookingResponseDto;
import com.springprojects.service.customer.booking.CustomerBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerBookingController {

    private final CustomerBookingService customerBookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestAttribute("userId") UUID userId,
                                            @Valid @RequestBody BookingRequestDto dto) {
        return customerBookingService.createBooking(userId, dto);
    }

    @GetMapping
    public List<BookingResponseDto> getBookings(@RequestAttribute("userId") UUID userId) {
        return customerBookingService.getBookingsByUser(userId);
    }

    @DeleteMapping("/{bookingId}")
    public void cancelBooking(@RequestAttribute("userId") UUID userId,
                              @PathVariable UUID bookingId) {
        customerBookingService.cancelBooking(userId, bookingId);
    }

}

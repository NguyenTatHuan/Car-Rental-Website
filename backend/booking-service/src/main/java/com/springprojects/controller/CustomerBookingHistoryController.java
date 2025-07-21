package com.springprojects.controller;

import com.springprojects.dto.BookingHistoryResponseDto;
import com.springprojects.service.customer.bookingHistory.CustomerBookingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/booking-history")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerBookingHistoryController {

    private final CustomerBookingHistoryService customerBookingHistoryService;

    @GetMapping
    public List<BookingHistoryResponseDto> getUserBookingHistories(
            @RequestAttribute("userId") UUID userId) {
        return customerBookingHistoryService.getBookingHistoriesByUser(userId);
    }

    @GetMapping("/{bookingId}")
    public List<BookingHistoryResponseDto> getBookingHistoryByBookingId(
            @PathVariable UUID bookingId) {
        return customerBookingHistoryService.getBookingHistoriesByBookingId(bookingId);
    }

}

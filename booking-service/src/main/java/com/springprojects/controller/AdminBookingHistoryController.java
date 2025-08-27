package com.springprojects.controller;

import com.springprojects.dto.BookingHistoryResponseDto;
import com.springprojects.service.admin.bookingHistory.AdminBookingHistoryService;
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
@RequestMapping("/api/admin/booking-history")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingHistoryController {

    private final AdminBookingHistoryService adminBookingHistoryService;

    public AdminBookingHistoryController(AdminBookingHistoryService adminBookingHistoryService) {
        this.adminBookingHistoryService = adminBookingHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<BookingHistoryResponseDto>> getAllHistories() {
        List<BookingHistoryResponseDto> histories = adminBookingHistoryService.getAllHistory();
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<BookingHistoryResponseDto>> getHistoriesByBookingId(@PathVariable UUID bookingId) {
        List<BookingHistoryResponseDto> histories = adminBookingHistoryService.getHistoryByBookingId(bookingId);
        return ResponseEntity.ok(histories);
    }

}

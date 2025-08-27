package com.springprojects.service.customer.bookingHistory;

import com.springprojects.dto.BookingHistoryResponseDto;
import com.springprojects.entity.BookingHistory;
import com.springprojects.repository.BookingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerBookingHistoryServiceImpl implements CustomerBookingHistoryService {

    private final BookingHistoryRepository bookingHistoryRepository;

    private BookingHistoryResponseDto entityToDto(BookingHistory bookingHistory) {
        return BookingHistoryResponseDto.builder()
                .id(bookingHistory.getId())
                .bookingId(bookingHistory.getBookingId())
                .userId(bookingHistory.getUserId())
                .carId(bookingHistory.getCarId())
                .startTime(bookingHistory.getStartTime())
                .endTime(bookingHistory.getEndTime())
                .totalPrice(bookingHistory.getTotalPrice())
                .status(bookingHistory.getStatus())
                .action(bookingHistory.getAction())
                .changedAt(bookingHistory.getChangedAt())
                .build();
    }

    @Override
    public List<BookingHistoryResponseDto> getBookingHistoriesByBookingId(UUID bookingId) {
        return bookingHistoryRepository.findByBookingIdOrderByChangedAtDesc(bookingId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingHistoryResponseDto> getBookingHistoriesByUser(UUID userId) {
        return bookingHistoryRepository.findByUserIdOrderByChangedAtDesc(userId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

}

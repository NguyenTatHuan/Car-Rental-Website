package com.springprojects.service.admin.bookingHistory;

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
public class AdminBookingHistoryServiceImpl implements AdminBookingHistoryService {

    private final BookingHistoryRepository bookingHistoryRepository;

    private BookingHistoryResponseDto entityToDto(BookingHistory history) {
        return BookingHistoryResponseDto.builder()
                .id(history.getId())
                .bookingId(history.getBookingId())
                .userId(history.getUserId())
                .carId(history.getCarId())
                .startTime(history.getStartTime())
                .endTime(history.getEndTime())
                .totalPrice(history.getTotalPrice())
                .status(history.getStatus())
                .action(history.getAction())
                .changedAt(history.getChangedAt())
                .build();
    }

    @Override
    public List<BookingHistoryResponseDto> getAllHistory() {
        return bookingHistoryRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingHistoryResponseDto> getHistoryByBookingId(UUID bookingId) {
        return bookingHistoryRepository.findByBookingIdOrderByChangedAtDesc(bookingId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

}

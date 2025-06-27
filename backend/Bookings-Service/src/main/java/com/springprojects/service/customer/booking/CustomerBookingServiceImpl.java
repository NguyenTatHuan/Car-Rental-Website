package com.springprojects.service.customer.booking;

import com.springprojects.client.CarClient;
import com.springprojects.dto.BookingRequestDto;
import com.springprojects.dto.BookingResponseDto;
import com.springprojects.entity.Booking;
import com.springprojects.entity.BookingHistory;
import com.springprojects.enums.BookingStatus;
import com.springprojects.enums.HistoryAction;
import com.springprojects.repository.BookingHistoryRepository;
import com.springprojects.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class CustomerBookingServiceImpl implements CustomerBookingService {

    private final BookingRepository bookingRepository;

    private final BookingHistoryRepository historyRepository;

    private final CarClient carClient;

    private BookingResponseDto entityToDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .carId(booking.getCarId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    @Override
    public BookingResponseDto createBooking(UUID userId, BookingRequestDto dto) {

        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new ResponseStatusException(BAD_REQUEST, "End date must be after start date!");
        }

        if (dto.getStartTime().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(BAD_REQUEST, "Start time must be in the future!");
        }

        double pricePerDay = carClient.getPricePerDay(dto.getCarId());
        long numberOfDays = ChronoUnit.DAYS.between(dto.getStartTime(), dto.getEndTime()) + 1;
        double totalPrice = pricePerDay * numberOfDays;

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCarId(dto.getCarId());
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(dto.getEndTime());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        booking = bookingRepository.save(booking);

        BookingHistory history = new BookingHistory();
        history.setBookingId(booking.getId());
        history.setUserId(userId);
        history.setCarId(dto.getCarId());
        history.setStartTime(dto.getStartTime());
        history.setEndTime(dto.getEndTime());
        history.setTotalPrice(totalPrice);
        history.setStatus(booking.getStatus());
        history.setAction(HistoryAction.CREATED);

        historyRepository.save(history);

        return entityToDto(booking);

    }

    @Override
    public List<BookingResponseDto> getBookingsByUser(UUID userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(UUID userId, UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new ResponseStatusException(FORBIDDEN, "You are not allowed to cancel this booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ResponseStatusException(BAD_REQUEST, "Only pending bookings can be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        BookingHistory history = new BookingHistory();
        history.setBookingId(bookingId);
        history.setUserId(userId);
        history.setCarId(booking.getCarId());
        history.setStartTime(booking.getStartTime());
        history.setEndTime(booking.getEndTime());
        history.setTotalPrice(booking.getTotalPrice());
        history.setStatus(BookingStatus.CANCELLED);
        history.setAction(HistoryAction.CANCELLED);

        historyRepository.save(history);
    }
}

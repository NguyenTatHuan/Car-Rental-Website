package com.springprojects.service.admin.booking;

import com.springprojects.client.CarClient;
import com.springprojects.client.UserClient;
import com.springprojects.dto.booking.BookingResponseDto;
import com.springprojects.dto.client.CarInformationDto;
import com.springprojects.dto.client.CustomerInformationDto;
import com.springprojects.dto.kafka.BookingResponseEvent;
import com.springprojects.entity.Booking;
import com.springprojects.entity.BookingHistory;
import com.springprojects.enums.BookingStatus;
import com.springprojects.enums.HistoryAction;
import com.springprojects.kafka.BookingEventProducer;
import com.springprojects.repository.BookingHistoryRepository;
import com.springprojects.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminBookingServiceImpl implements AdminBookingService {

    private final BookingRepository bookingRepository;

    private final BookingHistoryRepository historyRepository;

    private final UserClient userClient;

    private final CarClient carClient;

    private final BookingEventProducer bookingEventProducer;

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

    private void saveHistory(Booking booking, HistoryAction action) {
        BookingHistory history = new BookingHistory();
        history.setBookingId(booking.getId());
        history.setUserId(booking.getUserId());
        history.setCarId(booking.getCarId());
        history.setStartTime(booking.getStartTime());
        history.setEndTime(booking.getEndTime());
        history.setTotalPrice(booking.getTotalPrice());
        history.setStatus(booking.getStatus());
        history.setAction(action);
        historyRepository.save(history);
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    public BookingResponseDto getBookingById(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found booking with ID: " + id));
        return entityToDto(booking);
    }

    @Override
    public BookingResponseDto updateBookingStatus(UUID id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found booking with ID: " + id));
        booking.setStatus(status);
        Booking updated = bookingRepository.save(booking);
        saveHistory(updated, HistoryAction.UPDATED);

        CustomerInformationDto customerInformation = userClient.getCustomerInformation(updated.getUserId());
        CarInformationDto carInformation = carClient.getCarInformation(updated.getCarId());

        if (status == BookingStatus.CONFIRMED || status == BookingStatus.REJECTED) {
            BookingResponseEvent event = new BookingResponseEvent();
            event.setBookingId(updated.getId());
            event.setUserId(updated.getUserId());
            event.setCarId(updated.getCarId());
            event.setEmail(customerInformation.getEmail());
            event.setFullName(customerInformation.getFullName());
            event.setCarName(carInformation.getCarName());
            event.setTotalPrice(updated.getTotalPrice());
            event.setStartTime(updated.getStartTime());
            event.setEndTime(updated.getEndTime());
            event.setStatus(status);
            bookingEventProducer.sendBookingResponseEvent(event);
        }

        return entityToDto(updated);
    }

    @Override
    public void deleteBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found booking with ID: " + id));
        saveHistory(booking, HistoryAction.CANCELLED);
        bookingRepository.deleteById(id);
    }

}

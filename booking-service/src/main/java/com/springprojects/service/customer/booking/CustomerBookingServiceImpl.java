package com.springprojects.service.customer.booking;

import com.springprojects.client.CarClient;
import com.springprojects.client.UserClient;
import com.springprojects.dto.booking.BookingUpdateDto;
import com.springprojects.dto.booking.BookingRequestDto;
import com.springprojects.dto.booking.BookingResponseDto;
import com.springprojects.dto.client.CarInformationDto;
import com.springprojects.dto.client.CustomerInformationDto;
import com.springprojects.dto.kafka.BookingEvent;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerBookingServiceImpl implements CustomerBookingService {

    private final BookingRepository bookingRepository;

    private final BookingHistoryRepository historyRepository;

    private final CarClient carClient;

    private final UserClient userClient;

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

    @Override
    public BookingResponseDto createBooking(UUID userId, BookingRequestDto dto) {

        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date!");
        }

        if (dto.getStartTime().isBefore(LocalDate.now().plusDays(3))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must book at least 3 days before the start date!");
        }

        CarInformationDto car = carClient.getCarInformation(dto.getCarId());

        double pricePerDay = car.getPricePerDay();
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

        CustomerInformationDto userInfo = userClient.getCustomerInformation(userId);

        BookingEvent event = new BookingEvent();
        event.setBookingId(booking.getId());
        event.setUserId(userId);
        event.setCarId(dto.getCarId());
        event.setFullName(userInfo.getFullName());
        event.setEmail(userInfo.getEmail());
        event.setCarName(car.getCarName());
        event.setTotalPrice(totalPrice);
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());

        bookingEventProducer.sendBookingCreatedEvent(event);

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to cancel this booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending bookings can be cancelled");
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

        CustomerInformationDto userInfo = userClient.getCustomerInformation(userId);
        CarInformationDto car = carClient.getCarInformation(booking.getCarId());

        BookingEvent event = new BookingEvent();
        event.setBookingId(booking.getId());
        event.setUserId(userId);
        event.setCarId(booking.getCarId());
        event.setFullName(userInfo.getFullName());
        event.setEmail(userInfo.getEmail());
        event.setCarName(car.getCarName());
        event.setTotalPrice(booking.getTotalPrice());
        event.setStartTime(booking.getStartTime());
        event.setEndTime(booking.getEndTime());

        bookingEventProducer.sendBookingCancelledEvent(event);

    }

    @Override
    public BookingResponseDto updateBooking(UUID userId, UUID bookingId, BookingUpdateDto dto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found booking with id:" + bookingId));

        if (!booking.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending bookings can be updated");
        }

        if (dto.getStartTime() != null) {
            if (dto.getStartTime().isBefore(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be in the future");
            }
            booking.setStartTime(dto.getStartTime());
        }

        if (dto.getEndTime() != null) {
            LocalDate compareStart = dto.getStartTime() != null ? dto.getStartTime() : booking.getStartTime();
            if (dto.getEndTime().isBefore(compareStart)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
            }
            booking.setEndTime(dto.getEndTime());
        }

        if (dto.getCarId() != null) {
            booking.setCarId(dto.getCarId());
        }

        double pricePerDay = carClient.getCarInformation(booking.getCarId()).getPricePerDay();
        long numberOfDays = ChronoUnit.DAYS.between(booking.getStartTime(), booking.getEndTime()) + 1;
        double totalPrice = pricePerDay * numberOfDays;
        booking.setTotalPrice(totalPrice);

        booking = bookingRepository.save(booking);

        BookingHistory history = new BookingHistory();
        history.setBookingId(booking.getId());
        history.setUserId(userId);
        history.setCarId(booking.getCarId());
        history.setStartTime(booking.getStartTime());
        history.setEndTime(booking.getEndTime());
        history.setTotalPrice(booking.getTotalPrice());
        history.setStatus(booking.getStatus());
        history.setAction(HistoryAction.UPDATED);

        historyRepository.save(history);

        CustomerInformationDto userInfo = userClient.getCustomerInformation(userId);
        CarInformationDto car = carClient.getCarInformation(booking.getCarId());

        BookingEvent event = new BookingEvent();
        event.setBookingId(booking.getId());
        event.setUserId(userId);
        event.setCarId(booking.getCarId());
        event.setFullName(userInfo.getFullName());
        event.setEmail(userInfo.getEmail());
        event.setCarName(car.getCarName());
        event.setTotalPrice(totalPrice);
        event.setStartTime(booking.getStartTime());
        event.setEndTime(booking.getEndTime());

        bookingEventProducer.sendBookingUpdatedEvent(event);

        return entityToDto(booking);
    }

}

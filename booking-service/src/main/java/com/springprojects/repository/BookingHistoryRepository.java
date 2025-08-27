package com.springprojects.repository;

import com.springprojects.entity.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingHistoryRepository extends JpaRepository<BookingHistory, UUID> {

    List<BookingHistory> findByBookingIdOrderByChangedAtDesc(UUID bookingId);

    List<BookingHistory> findByUserIdOrderByChangedAtDesc(UUID userId);

}

package com.springprojects.repository;

import com.springprojects.dto.BookingResponseDto;
import com.springprojects.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByUserId(UUID userId);

}

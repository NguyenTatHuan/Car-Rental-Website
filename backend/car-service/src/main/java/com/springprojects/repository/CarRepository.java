package com.springprojects.repository;

import com.springprojects.entity.Car;
import com.springprojects.enums.CarStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

    boolean existsByLicensePlate(String licensePlate);

    List<Car> findByStatus(CarStatus status);

}

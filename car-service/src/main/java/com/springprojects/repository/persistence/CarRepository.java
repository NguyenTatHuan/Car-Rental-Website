package com.springprojects.repository.persistence;

import com.springprojects.entity.Car;
import com.springprojects.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

    boolean existsByLicensePlate(String licensePlate);

    List<Car> findByStatus(CarStatus status);

}

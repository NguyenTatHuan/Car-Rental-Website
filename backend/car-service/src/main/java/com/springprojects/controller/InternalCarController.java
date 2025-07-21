package com.springprojects.controller;

import com.springprojects.dto.CarInformationDto;
import com.springprojects.entity.Car;
import com.springprojects.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/internal/car")
@RequiredArgsConstructor
public class InternalCarController {

    private final CarRepository carRepository;

    @GetMapping("/{id}/information")
    public CarInformationDto getCarInformation(@PathVariable UUID id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found car with id:" + id));

        String carName = car.getBrand().getName() + " " + car.getModel() + " - " + car.getLicensePlate();

        return CarInformationDto.builder()
                .carName(carName)
                .pricePerDay(car.getPricePerDay())
                .build();
    }

}

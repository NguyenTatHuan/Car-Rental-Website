package com.springprojects.service.customer.car;

import com.springprojects.dto.car.CarCustomerDto;
import com.springprojects.entity.Car;
import com.springprojects.enums.CarStatus;
import com.springprojects.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerCarServiceImpl implements CustomerCarService {

    private final CarRepository carRepository;

    private CarCustomerDto entityToDto(Car car) {
        CarCustomerDto dto = new CarCustomerDto();
        dto.setId(car.getId());
        dto.setModel(car.getModel());
        dto.setFuelType(car.getFuelType());
        dto.setTransmissionType(car.getTransmissionType());
        dto.setYear(car.getYear());
        dto.setSeats(car.getSeats());
        dto.setColor(car.getColor());
        dto.setPricePerDay(car.getPricePerDay());
        dto.setImageUrl(car.getImageUrl());

        dto.setBrandName(car.getBrand().getName());
        dto.setCarTypeName(car.getCarType().getName());
        dto.setLocationName(car.getLocation().getName());

        return dto;
    }

    @Override
    public List<CarCustomerDto> getAvailableCars() {
        return carRepository.findByStatus(CarStatus.AVAILABLE)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CarCustomerDto getCarDetail(UUID id) {
        Car car = carRepository.findById(id)
                .filter(c -> c.getStatus() == CarStatus.AVAILABLE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found or not available"));
        return entityToDto(car);
    }

}

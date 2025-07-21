package com.springprojects.service.admin.car;

import com.springprojects.dto.car.CarCreateDto;
import com.springprojects.dto.car.CarResponseDto;
import com.springprojects.dto.car.CarUpdateDto;
import com.springprojects.entity.Brand;
import com.springprojects.entity.Car;
import com.springprojects.entity.CarType;
import com.springprojects.entity.Location;
import com.springprojects.repository.BrandRepository;
import com.springprojects.repository.CarRepository;
import com.springprojects.repository.CarTypeRepository;
import com.springprojects.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCarServiceImpl implements AdminCarService {

    private final CarRepository carRepository;

    private final BrandRepository brandRepository;

    private final CarTypeRepository carTypeRepository;

    private final LocationRepository locationRepository;

    private CarResponseDto entityToDto(Car car) {
        CarResponseDto dto = new CarResponseDto();
        dto.setId(car.getId());
        dto.setLicensePlate(car.getLicensePlate());
        dto.setModel(car.getModel());
        dto.setFuelType(car.getFuelType());
        dto.setTransmissionType(car.getTransmissionType());
        dto.setYear(car.getYear());
        dto.setSeats(car.getSeats());
        dto.setColor(car.getColor());
        dto.setPricePerDay(car.getPricePerDay());
        dto.setImageUrl(car.getImageUrl());
        dto.setStatus(car.getStatus());

        dto.setBrandId(car.getBrand().getId());
        dto.setBrandName(car.getBrand().getName());

        dto.setCarTypeId(car.getCarType().getId());
        dto.setCarTypeName(car.getCarType().getName());

        dto.setLocationId(car.getLocation().getId());
        dto.setLocationName(car.getLocation().getName());

        return dto;
    }

    @Override
    public CarResponseDto createCar(CarCreateDto dto) {
        if (carRepository.existsByLicensePlate(dto.getLicensePlate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "License plate already exists");
        }

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));

        CarType carType = carTypeRepository.findById(dto.getCarTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car type not found"));

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        Car car = new Car();
        car.setLicensePlate(dto.getLicensePlate());
        car.setModel(dto.getModel());
        car.setFuelType(dto.getFuelType());
        car.setTransmissionType(dto.getTransmissionType());
        car.setYear(dto.getYear());
        car.setSeats(dto.getSeats());
        car.setColor(dto.getColor());
        car.setPricePerDay(dto.getPricePerDay());
        car.setImageUrl(dto.getImageUrl());
        car.setStatus(dto.getStatus());
        car.setBrand(brand);
        car.setCarType(carType);
        car.setLocation(location);

        return entityToDto(carRepository.save(car));
    }

    @Override
    public CarResponseDto updateCar(UUID id, CarUpdateDto dto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        if (dto.getLicensePlate() != null) car.setLicensePlate(dto.getLicensePlate());
        if (dto.getModel() != null) car.setModel(dto.getModel());
        if (dto.getFuelType() != null) car.setFuelType(dto.getFuelType());
        if (dto.getTransmissionType() != null) car.setTransmissionType(dto.getTransmissionType());
        if (dto.getYear() != null) car.setYear(dto.getYear());
        if (dto.getSeats() != null) car.setSeats(dto.getSeats());
        if (dto.getColor() != null) car.setColor(dto.getColor());
        if (dto.getPricePerDay() != null) car.setPricePerDay(dto.getPricePerDay());
        if (dto.getImageUrl() != null) car.setImageUrl(dto.getImageUrl());
        if (dto.getStatus() != null) car.setStatus(dto.getStatus());

        if (dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));
            car.setBrand(brand);
        }

        if (dto.getCarTypeId() != null) {
            CarType carType = carTypeRepository.findById(dto.getCarTypeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car type not found"));
            car.setCarType(carType);
        }

        if (dto.getLocationId() != null) {
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));
            car.setLocation(location);
        }

        return entityToDto(carRepository.save(car));
    }

    @Override
    public void deleteCar(UUID id) {
        if (!carRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found");
        }
        carRepository.deleteById(id);
    }

    @Override
    public CarResponseDto getCarById(UUID id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
        return entityToDto(car);
    }

    @Override
    public List<CarResponseDto> getAllCars() {
        return carRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}

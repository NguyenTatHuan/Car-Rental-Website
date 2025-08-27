package com.springprojects.service.admin.car;

import com.springprojects.dto.car.CarCreateDto;
import com.springprojects.dto.car.CarResponseDto;
import com.springprojects.dto.car.CarUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AdminCarService {

    CarResponseDto createCar(CarCreateDto dto);

    CarResponseDto updateCar(UUID id, CarUpdateDto dto);

    void deleteCar(UUID id);

    CarResponseDto getCarById(UUID id);

    List<CarResponseDto> getAllCars();

}

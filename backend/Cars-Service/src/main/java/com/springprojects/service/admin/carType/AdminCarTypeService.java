package com.springprojects.service.admin.carType;

import com.springprojects.dto.cartype.CarTypeCreateDto;
import com.springprojects.dto.cartype.CarTypeResponseDto;
import com.springprojects.dto.cartype.CarTypeUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AdminCarTypeService {

    CarTypeResponseDto createCarType(CarTypeCreateDto carTypeCreateDto);

    CarTypeResponseDto updateCarType(UUID id, CarTypeUpdateDto carTypeUpdateDto);

    void deleteCarType(UUID id);

    CarTypeResponseDto getCarTypeById(UUID id);

    List<CarTypeResponseDto> getAllCarTypes();

}

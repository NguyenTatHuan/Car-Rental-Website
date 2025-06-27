package com.springprojects.service.admin.carType;

import com.springprojects.dto.cartype.CarTypeCreateDto;
import com.springprojects.dto.cartype.CarTypeResponseDto;
import com.springprojects.dto.cartype.CarTypeUpdateDto;
import com.springprojects.entity.CarType;
import com.springprojects.repository.CarTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCarTypeServiceImpl implements AdminCarTypeService {

    private final CarTypeRepository carTypeRepository;

    private CarTypeResponseDto entityToDto(CarType carType) {
        CarTypeResponseDto carTypeResponseDto = new CarTypeResponseDto();
        carTypeResponseDto.setId(carType.getId());
        carTypeResponseDto.setName(carType.getName());
        carTypeResponseDto.setDescription(carType.getDescription());
        return carTypeResponseDto;
    }

    @Override
    public CarTypeResponseDto createCarType(CarTypeCreateDto carTypeCreateDto) {
        if (carTypeRepository.existsByName(carTypeCreateDto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CarType name already exists!");
        }
        CarType carType = new CarType();
        carType.setName(carTypeCreateDto.getName());
        carType.setDescription(carTypeCreateDto.getDescription());
        return entityToDto(carTypeRepository.save(carType));
    }

    @Override
    public CarTypeResponseDto updateCarType(UUID id, CarTypeUpdateDto carTypeUpdateDto) {
        CarType carType = carTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found CarType with id:" + id));
        if (carTypeRepository.existsByNameAndIdNot(carTypeUpdateDto.getName(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Car type name already used by another record");
        }
        carType.setName(carTypeUpdateDto.getName());
        carType.setDescription(carTypeUpdateDto.getDescription());
        return entityToDto(carTypeRepository.save(carType));
    }

    @Override
    public void deleteCarType(UUID id) {
        if (!carTypeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found CarType with id:" + id);
        }
        carTypeRepository.deleteById(id);
    }

    @Override
    public CarTypeResponseDto getCarTypeById(UUID id) {
        CarType carType = carTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found CarType with id:" + id));
        return entityToDto(carType);
    }

    @Override
    public List<CarTypeResponseDto> getAllCarTypes() {
        return carTypeRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}

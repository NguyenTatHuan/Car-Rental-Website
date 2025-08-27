package com.springprojects.service.admin.carType;

import com.springprojects.dto.cartype.CarTypeCreateDto;
import com.springprojects.dto.cartype.CarTypeResponseDto;
import com.springprojects.dto.cartype.CarTypeUpdateDto;
import com.springprojects.elasticsearch.CarTypeDocument;
import com.springprojects.entity.CarType;
import com.springprojects.repository.elasticsearch.CarTypeElasticsearchRepository;
import com.springprojects.repository.persistence.CarTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCarTypeServiceImpl implements AdminCarTypeService {

    private final CarTypeRepository carTypeRepository;

    private final CarTypeElasticsearchRepository carTypeElasticsearchRepository;

    private CarTypeResponseDto mapToDto(CarType carType) {
        return CarTypeResponseDto.builder()
                .id(carType.getId())
                .name(carType.getName())
                .description(carType.getDescription())
                .build();
    }

    private CarTypeDocument mapToDocument(CarType carType) {
        return CarTypeDocument.builder()
                .id(carType.getId())
                .name(carType.getName())
                .description(carType.getDescription())
                .build();
    }

    @Override
    @Transactional
    public CarTypeResponseDto createCarType(CarTypeCreateDto carTypeCreateDto) {
        if (carTypeRepository.existsByName(carTypeCreateDto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CarType name already exists!");
        }

        CarType carType = CarType.builder()
                .name(carTypeCreateDto.getName())
                .description(carTypeCreateDto.getDescription())
                .build();

        CarType saved = carTypeRepository.save(carType);
        carTypeElasticsearchRepository.save(mapToDocument(saved));

        return mapToDto(saved);
    }

    @Override
    @Transactional
    public CarTypeResponseDto updateCarType(UUID id, CarTypeUpdateDto carTypeUpdateDto) {
        CarType carType = carTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found CarType with id:" + id));

        if (carTypeRepository.existsByNameAndIdNot(carTypeUpdateDto.getName(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Car type name already used by another record");
        }

        if (carTypeUpdateDto.getName() != null) {
            carType.setName(carTypeUpdateDto.getName());
        }

        if (carTypeUpdateDto.getDescription() != null) {
            carType.setDescription(carTypeUpdateDto.getDescription());
        }

        CarType updated = carTypeRepository.save(carType);
        carTypeElasticsearchRepository.save(mapToDocument(updated));

        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void deleteCarType(UUID id) {
        if (!carTypeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found CarType with id:" + id);
        }

        carTypeRepository.deleteById(id);
        carTypeElasticsearchRepository.deleteById(id);
    }

    @Override
    public CarTypeResponseDto getCarTypeById(UUID id) {
        CarType carType = carTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found CarType with id:" + id));
        return mapToDto(carType);
    }

    @Override
    public List<CarTypeResponseDto> getAllCarTypes() {
        return carTypeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}

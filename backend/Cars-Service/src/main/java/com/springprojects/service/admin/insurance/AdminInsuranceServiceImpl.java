package com.springprojects.service.admin.insurance;

import com.springprojects.dto.insurance.InsuranceCreateDto;
import com.springprojects.dto.insurance.InsuranceResponseDto;
import com.springprojects.dto.insurance.InsuranceUpdateDto;
import com.springprojects.entity.Car;
import com.springprojects.entity.Insurance;
import com.springprojects.repository.CarRepository;
import com.springprojects.repository.CarTypeRepository;
import com.springprojects.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInsuranceServiceImpl implements AdminInsuranceService {

    private final InsuranceRepository insuranceRepository;

    private final CarRepository carRepository;

    private InsuranceResponseDto entityToDto(Insurance insurance) {
        InsuranceResponseDto dto = new InsuranceResponseDto();
        dto.setId(insurance.getId());
        dto.setCarId(insurance.getCar().getId());
        dto.setProvider(insurance.getProvider());
        dto.setPrice(insurance.getPrice());
        dto.setStartDate(insurance.getStartDate());
        dto.setEndDate(insurance.getEndDate());
        return dto;
    }

    @Override
    public InsuranceResponseDto createInsurance(InsuranceCreateDto insuranceCreateDto) {
        if (insuranceCreateDto.getEndDate().isBefore(insuranceCreateDto.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
        }
        Car car = carRepository.findById(insuranceCreateDto.getCarId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found car with id:" + insuranceCreateDto.getCarId()));
        Insurance insurance = new Insurance();
        insurance.setCar(car);
        insurance.setProvider(insuranceCreateDto.getProvider());
        insurance.setPrice(insuranceCreateDto.getPrice());
        insurance.setStartDate(insuranceCreateDto.getStartDate());
        insurance.setEndDate(insuranceCreateDto.getEndDate());
        return entityToDto(insuranceRepository.save(insurance));
    }

    @Override
    public InsuranceResponseDto updateInsurance(UUID id, InsuranceUpdateDto insuranceUpdateDto) {
        Insurance insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not found Insurance with id: " + id));
        if (insuranceUpdateDto.getStartDate() != null && insuranceUpdateDto.getEndDate() != null) {
            if (insuranceUpdateDto.getEndDate().isBefore(insuranceUpdateDto.getStartDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
            }
            insurance.setStartDate(insuranceUpdateDto.getStartDate());
            insurance.setEndDate(insuranceUpdateDto.getEndDate());
        }

        if (insuranceUpdateDto.getCarId() != null && !insurance.getCar().getId().equals(insuranceUpdateDto.getCarId())) {
            Car car = carRepository.findById(insuranceUpdateDto.getCarId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
            insurance.setCar(car);
        }

        if (insuranceUpdateDto.getProvider() != null) {
            insurance.setProvider(insuranceUpdateDto.getProvider());
        }

        if (insuranceUpdateDto.getPrice() != null) {
            insurance.setPrice(insuranceUpdateDto.getPrice());
        }

        return entityToDto(insuranceRepository.save(insurance));
    }

    @Override
    public void deleteInsurance(UUID id) {
        if (!insuranceRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Insurance not found");
        }
        insuranceRepository.deleteById(id);
    }

    @Override
    public InsuranceResponseDto getInsuranceById(UUID id) {
        Insurance insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Insurance not found"));
        return entityToDto(insurance);
    }

    @Override
    public List<InsuranceResponseDto> getInsuranceByCarId(UUID carId) {
        List<Insurance> insurances = insuranceRepository.findAllByCar_Id(carId);
        if (insurances.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No insurance found for car with id: " + carId);
        }
        return insurances.stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    public List<InsuranceResponseDto> getAllInsurances() {
        return insuranceRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}

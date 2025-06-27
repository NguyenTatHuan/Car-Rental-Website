package com.springprojects.service.admin.maintenance;

import com.springprojects.dto.maintenance.MaintenanceCreateDto;
import com.springprojects.dto.maintenance.MaintenanceResponseDto;
import com.springprojects.dto.maintenance.MaintenanceUpdateDto;
import com.springprojects.entity.Car;
import com.springprojects.entity.Maintenance;
import com.springprojects.repository.CarRepository;
import com.springprojects.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMaintenanceServiceImpl implements AdminMaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    private final CarRepository carRepository;

    private MaintenanceResponseDto entityToDto(Maintenance maintenance) {
        MaintenanceResponseDto dto = new MaintenanceResponseDto();
        dto.setId(maintenance.getId());
        dto.setCarId(maintenance.getCar().getId());
        dto.setDescription(maintenance.getDescription());
        dto.setCost(maintenance.getCost());
        dto.setServicedBy(maintenance.getServicedBy());
        dto.setStartDate(maintenance.getStartDate());
        dto.setEndDate(maintenance.getEndDate());
        return dto;
    }

    @Override
    public MaintenanceResponseDto createMaintenance(MaintenanceCreateDto createDto) {
        if (createDto.getEndDate().isBefore(createDto.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
        }
        Car car = carRepository.findById(createDto.getCarId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
        Maintenance maintenance = new Maintenance();
        maintenance.setCar(car);
        maintenance.setDescription(createDto.getDescription());
        maintenance.setCost(createDto.getCost());
        maintenance.setServicedBy(createDto.getServicedBy());
        maintenance.setStartDate(createDto.getStartDate());
        maintenance.setEndDate(createDto.getEndDate());
        return entityToDto(maintenanceRepository.save(maintenance));
    }

    @Override
    public MaintenanceResponseDto updateMaintenance(UUID id, MaintenanceUpdateDto updateDto) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance not found"));

        if (updateDto.getCarId() != null && !updateDto.getCarId().equals(maintenance.getCar().getId())) {
            Car car = carRepository.findById(updateDto.getCarId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
            maintenance.setCar(car);
        }

        if (updateDto.getStartDate() != null && updateDto.getEndDate() != null) {
            if (updateDto.getEndDate().isBefore(updateDto.getStartDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
            }
            maintenance.setStartDate(updateDto.getStartDate());
            maintenance.setEndDate(updateDto.getEndDate());
        }

        if (updateDto.getDescription() != null) {
            maintenance.setDescription(updateDto.getDescription());
        }
        if (updateDto.getCost() != null) {
            maintenance.setCost(updateDto.getCost());
        }
        if (updateDto.getServicedBy() != null) {
            maintenance.setServicedBy(updateDto.getServicedBy());
        }

        return entityToDto(maintenanceRepository.save(maintenance));
    }

    @Override
    public void deleteMaintenance(UUID id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance not found");
        }
        maintenanceRepository.deleteById(id);
    }

    @Override
    public MaintenanceResponseDto getMaintenanceById(UUID id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance not found"));
        return entityToDto(maintenance);
    }

    @Override
    public List<MaintenanceResponseDto> getAllMaintenances() {
        return maintenanceRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceResponseDto> getMaintenancesByCarId(UUID carId) {
        List<Maintenance> maintenances = maintenanceRepository.findAllByCar_Id(carId);
        if (maintenances.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No maintenance found for car with id: " + carId);
        }
        return maintenances.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}

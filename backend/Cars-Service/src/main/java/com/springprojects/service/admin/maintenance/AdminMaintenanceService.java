package com.springprojects.service.admin.maintenance;

import com.springprojects.dto.maintenance.MaintenanceCreateDto;
import com.springprojects.dto.maintenance.MaintenanceResponseDto;
import com.springprojects.dto.maintenance.MaintenanceUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AdminMaintenanceService {

    MaintenanceResponseDto createMaintenance(MaintenanceCreateDto createDto);

    MaintenanceResponseDto updateMaintenance(UUID id, MaintenanceUpdateDto updateDto);

    void deleteMaintenance(UUID id);

    MaintenanceResponseDto getMaintenanceById(UUID id);

    List<MaintenanceResponseDto> getAllMaintenances();

    List<MaintenanceResponseDto> getMaintenancesByCarId(UUID carId);

}

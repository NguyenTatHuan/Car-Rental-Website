package com.springprojects.controller;

import com.springprojects.dto.maintenance.MaintenanceCreateDto;
import com.springprojects.dto.maintenance.MaintenanceResponseDto;
import com.springprojects.dto.maintenance.MaintenanceUpdateDto;
import com.springprojects.service.admin.maintenance.AdminMaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/maintenances")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMaintenanceController {

    private final AdminMaintenanceService adminMaintenanceService;

    @PostMapping
    public ResponseEntity<MaintenanceResponseDto> createMaintenance(
            @Valid @RequestBody MaintenanceCreateDto dto) {
        return ResponseEntity.ok(adminMaintenanceService.createMaintenance(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDto> updateMaintenance(
            @PathVariable UUID id,
            @RequestBody @Valid MaintenanceUpdateDto dto) {
        MaintenanceResponseDto updated = adminMaintenanceService.updateMaintenance(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable UUID id) {
        adminMaintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminMaintenanceService.getMaintenanceById(id));
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceResponseDto>> getAllMaintenances() {
        return ResponseEntity.ok(adminMaintenanceService.getAllMaintenances());
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<MaintenanceResponseDto>> getMaintenanceByCarId(@PathVariable UUID carId) {
        return ResponseEntity.ok(adminMaintenanceService.getMaintenancesByCarId(carId));
    }

}

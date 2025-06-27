package com.springprojects.controller;

import com.springprojects.dto.insurance.InsuranceCreateDto;
import com.springprojects.dto.insurance.InsuranceResponseDto;
import com.springprojects.dto.insurance.InsuranceUpdateDto;
import com.springprojects.service.admin.insurance.AdminInsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/insurances")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminInsuranceController {

    private final AdminInsuranceService adminInsuranceService;

    @PostMapping
    public ResponseEntity<InsuranceResponseDto> createInsurance(@Valid @RequestBody InsuranceCreateDto dto) {
        return ResponseEntity.ok(adminInsuranceService.createInsurance(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsuranceResponseDto> updateInsurance(
            @PathVariable UUID id,
            @Valid @RequestBody InsuranceUpdateDto dto
    ) {
        return ResponseEntity.ok(adminInsuranceService.updateInsurance(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable UUID id) {
        adminInsuranceService.deleteInsurance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceResponseDto> getInsuranceById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminInsuranceService.getInsuranceById(id));
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<InsuranceResponseDto>> getInsurancesByCarId(@PathVariable UUID carId) {
        return ResponseEntity.ok(adminInsuranceService.getInsuranceByCarId(carId));
    }

    @GetMapping
    public ResponseEntity<List<InsuranceResponseDto>> getAllInsurances() {
        return ResponseEntity.ok(adminInsuranceService.getAllInsurances());
    }

}

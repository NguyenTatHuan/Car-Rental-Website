package com.springprojects.controller;

import com.springprojects.dto.cartype.CarTypeCreateDto;
import com.springprojects.dto.cartype.CarTypeResponseDto;
import com.springprojects.dto.cartype.CarTypeUpdateDto;
import com.springprojects.service.admin.carType.AdminCarTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/car-types")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCarTypeController {

    private final AdminCarTypeService adminCarTypeService;

    @PostMapping
    public ResponseEntity<CarTypeResponseDto> createCarType(
            @Valid @RequestBody CarTypeCreateDto carTypeCreateDto
    ) {
        CarTypeResponseDto response = adminCarTypeService.createCarType(carTypeCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarTypeResponseDto> updateCarType(
            @PathVariable UUID id,
            @Valid @RequestBody CarTypeUpdateDto carTypeUpdateDto
    ) {
        CarTypeResponseDto response = adminCarTypeService.updateCarType(id, carTypeUpdateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarType(@PathVariable UUID id) {
        adminCarTypeService.deleteCarType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarTypeResponseDto> getCarTypeById(@PathVariable UUID id) {
        CarTypeResponseDto response = adminCarTypeService.getCarTypeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CarTypeResponseDto>> getAllCarTypes() {
        List<CarTypeResponseDto> responseList = adminCarTypeService.getAllCarTypes();
        return ResponseEntity.ok(responseList);
    }

}

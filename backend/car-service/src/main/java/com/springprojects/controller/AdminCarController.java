package com.springprojects.controller;

import com.springprojects.dto.car.CarCreateDto;
import com.springprojects.dto.car.CarResponseDto;
import com.springprojects.dto.car.CarUpdateDto;
import com.springprojects.service.admin.car.AdminCarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/car")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCarController {

    private final AdminCarService adminCarService;

    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@Valid @RequestBody CarCreateDto dto) {
        CarResponseDto created = adminCarService.createCar(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDto> updateCar(
            @PathVariable UUID id,
            @Valid @RequestBody CarUpdateDto dto
    ) {
        CarResponseDto updated = adminCarService.updateCar(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        adminCarService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable UUID id) {
        CarResponseDto car = adminCarService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAllCars() {
        List<CarResponseDto> cars = adminCarService.getAllCars();
        return ResponseEntity.ok(cars);
    }

}

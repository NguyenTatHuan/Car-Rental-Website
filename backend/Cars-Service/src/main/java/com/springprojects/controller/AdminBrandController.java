package com.springprojects.controller;

import com.springprojects.dto.brand.BrandCreateDto;
import com.springprojects.dto.brand.BrandResponseDto;
import com.springprojects.dto.brand.BrandUpdateDto;
import com.springprojects.service.admin.brand.AdminBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/brands")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminBrandController {

    private final AdminBrandService adminBrandService;

    @PostMapping
    public ResponseEntity<BrandResponseDto> createBrand(@Valid @RequestBody BrandCreateDto brandCreateDto) {
        BrandResponseDto response = adminBrandService.createBrand(brandCreateDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponseDto> updateBrand(
            @PathVariable UUID id,
            @Valid @RequestBody BrandUpdateDto brandUpdateDto) {
        BrandResponseDto response = adminBrandService.updateBrand(id, brandUpdateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable UUID id) {
        adminBrandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDto> getBrandById(@PathVariable UUID id) {
        BrandResponseDto response = adminBrandService.getBrandById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BrandResponseDto>> getAllBrands() {
        List<BrandResponseDto> list = adminBrandService.getAllBrands();
        return ResponseEntity.ok(list);
    }

}

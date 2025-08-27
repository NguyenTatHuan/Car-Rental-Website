package com.springprojects.controller;

import com.springprojects.dto.location.LocationCreateDto;
import com.springprojects.dto.location.LocationResponseDto;
import com.springprojects.dto.location.LocationUpdateDto;
import com.springprojects.elasticsearch.LocationDocument;
import com.springprojects.service.admin.location.AdminLocationService;
import com.springprojects.service.elasticsearch.location.LocationSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/location")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminLocationController {

    private final AdminLocationService locationService;

    private final LocationSearchService locationSearchService;

    @PostMapping
    public ResponseEntity<LocationResponseDto> createLocation(@Valid @RequestBody LocationCreateDto dto) {
        LocationResponseDto created = locationService.createLocation(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDto> updateLocation(
            @PathVariable UUID id,
            @Valid @RequestBody LocationUpdateDto dto) {
        LocationResponseDto updated = locationService.updateLocation(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable UUID id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDto> getLocationById(@PathVariable UUID id) {
        LocationResponseDto location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> getAllLocations() {
        List<LocationResponseDto> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationDocument>> searchLocations(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(locationSearchService.searchByKeyword(keyword.trim()));
    }

}

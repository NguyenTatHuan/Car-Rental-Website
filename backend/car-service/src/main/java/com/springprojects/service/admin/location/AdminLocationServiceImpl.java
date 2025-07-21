package com.springprojects.service.admin.location;

import com.springprojects.dto.location.LocationCreateDto;
import com.springprojects.dto.location.LocationResponseDto;
import com.springprojects.dto.location.LocationUpdateDto;
import com.springprojects.entity.Location;
import com.springprojects.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminLocationServiceImpl implements AdminLocationService {

    private final LocationRepository locationRepository;

    private LocationResponseDto entityToDto(Location location) {
        LocationResponseDto dto = new LocationResponseDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setProvince(location.getProvince());
        dto.setDistrict(location.getDistrict());
        dto.setAddress(location.getAddress());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        return dto;
    }

    @Override
    public LocationResponseDto createLocation(LocationCreateDto dto) {
        if (locationRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location name already exists");
        }

        Location location = new Location();
        location.setName(dto.getName());
        location.setProvince(dto.getProvince());
        location.setDistrict(dto.getDistrict());
        location.setAddress(dto.getAddress());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());

        return entityToDto(locationRepository.save(location));
    }

    @Override
    public LocationResponseDto updateLocation(UUID id, LocationUpdateDto dto) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        if (dto.getName() != null) location.setName(dto.getName());
        if (dto.getProvince() != null) location.setProvince(dto.getProvince());
        if (dto.getDistrict() != null) location.setDistrict(dto.getDistrict());
        if (dto.getAddress() != null) location.setAddress(dto.getAddress());
        if (dto.getLatitude() != null) location.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) location.setLongitude(dto.getLongitude());

        return entityToDto(locationRepository.save(location));
    }

    @Override
    public void deleteLocation(UUID id) {
        if (!locationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found location with id:" + id);
        }
        locationRepository.deleteById(id);
    }

    @Override
    public LocationResponseDto getLocationById(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found location with id:" + id));
        return entityToDto(location);
    }

    @Override
    public List<LocationResponseDto> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}

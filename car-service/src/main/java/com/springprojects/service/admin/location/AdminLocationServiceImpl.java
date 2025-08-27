package com.springprojects.service.admin.location;

import com.springprojects.dto.location.LocationCreateDto;
import com.springprojects.dto.location.LocationResponseDto;
import com.springprojects.dto.location.LocationUpdateDto;
import com.springprojects.elasticsearch.LocationDocument;
import com.springprojects.entity.Location;
import com.springprojects.repository.elasticsearch.LocationElasticsearchRepository;
import com.springprojects.repository.persistence.LocationRepository;
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

    private final LocationElasticsearchRepository locationElasticsearchRepository;

    private LocationResponseDto mapToDto(Location location) {
        return LocationResponseDto.builder()
                .id(location.getId())
                .name(location.getName())
                .province(location.getProvince())
                .district(location.getDistrict())
                .address(location.getAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    private LocationDocument mapToDocument(Location location) {
        return LocationDocument.builder()
                .id(location.getId())
                .name(location.getName())
                .province(location.getProvince())
                .district(location.getDistrict())
                .address(location.getAddress())
                .build();
    }

    @Override
    public LocationResponseDto createLocation(LocationCreateDto dto) {
        if (locationRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location name already exists");
        }

        Location location = Location.builder()
                .name(dto.getName())
                .province(dto.getProvince())
                .district(dto.getDistrict())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();

        Location saved = locationRepository.save(location);
        locationElasticsearchRepository.save(mapToDocument(saved));

        return mapToDto(saved);
    }

    @Override
    public LocationResponseDto updateLocation(UUID id, LocationUpdateDto dto) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found!"));

        if (dto.getName() != null && !dto.getName().equals(location.getName())) {
            if (locationRepository.existsByName(dto.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location name already exists!");
            }
            location.setName(dto.getName());
        }
        if (dto.getProvince() != null) location.setProvince(dto.getProvince());
        if (dto.getDistrict() != null) location.setDistrict(dto.getDistrict());
        if (dto.getAddress() != null) location.setAddress(dto.getAddress());
        if (dto.getLatitude() != null) location.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) location.setLongitude(dto.getLongitude());

        Location saved = locationRepository.save(location);
        locationElasticsearchRepository.save(mapToDocument(saved));

        return mapToDto(saved);
    }

    @Override
    public void deleteLocation(UUID id) {
        if (!locationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found location with id:" + id);
        }

        locationRepository.deleteById(id);
        locationElasticsearchRepository.deleteById(id);
    }

    @Override
    public LocationResponseDto getLocationById(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found location with id:" + id));
        return mapToDto(location);
    }

    @Override
    public List<LocationResponseDto> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}

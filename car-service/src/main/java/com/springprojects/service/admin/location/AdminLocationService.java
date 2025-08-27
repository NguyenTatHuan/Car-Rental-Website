package com.springprojects.service.admin.location;

import com.springprojects.dto.location.LocationCreateDto;
import com.springprojects.dto.location.LocationResponseDto;
import com.springprojects.dto.location.LocationUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AdminLocationService {

    LocationResponseDto createLocation(LocationCreateDto dto);

    LocationResponseDto updateLocation(UUID id, LocationUpdateDto dto);

    void deleteLocation(UUID id);

    LocationResponseDto getLocationById(UUID id);

    List<LocationResponseDto> getAllLocations();

}

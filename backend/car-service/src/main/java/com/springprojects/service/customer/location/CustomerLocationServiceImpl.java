package com.springprojects.service.customer.location;

import com.springprojects.dto.location.LocationCustomerDto;
import com.springprojects.entity.Location;
import com.springprojects.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerLocationServiceImpl implements CustomerLocationService {

    private final LocationRepository locationRepository;

    private LocationCustomerDto entityToDto(Location location) {
        LocationCustomerDto dto = new LocationCustomerDto();
        dto.setName(location.getName());
        dto.setProvince(location.getProvince());
        dto.setDistrict(location.getDistrict());
        dto.setAddress(location.getAddress());
        return dto;
    }

    @Override
    public List<LocationCustomerDto> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

}

package com.springprojects.controller;

import com.springprojects.dto.location.LocationCustomerDto;
import com.springprojects.service.customer.location.CustomerLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer/locations")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerLocationController {

    private final CustomerLocationService customerLocationService;

    @GetMapping
    public ResponseEntity<List<LocationCustomerDto>> getAllLocations() {
        List<LocationCustomerDto> locations = customerLocationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

}

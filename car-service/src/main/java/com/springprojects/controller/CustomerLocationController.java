package com.springprojects.controller;

import com.springprojects.dto.location.LocationCustomerDto;
import com.springprojects.elasticsearch.LocationDocument;
import com.springprojects.service.customer.location.CustomerLocationService;
import com.springprojects.service.elasticsearch.location.LocationSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer/location")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerLocationController {

    private final CustomerLocationService customerLocationService;

    private final LocationSearchService locationSearchService;

    @GetMapping
    public ResponseEntity<List<LocationCustomerDto>> getAllLocations() {
        List<LocationCustomerDto> locations = customerLocationService.getAllLocations();
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

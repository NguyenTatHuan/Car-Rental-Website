package com.springprojects.controller;

import com.springprojects.dto.car.CarCustomerDto;
import com.springprojects.service.customer.car.CustomerCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/cars")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerCarController {

    private final CustomerCarService customerCarService;

    @GetMapping
    public ResponseEntity<List<CarCustomerDto>> getAvailableCars() {
        List<CarCustomerDto> cars = customerCarService.getAvailableCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarCustomerDto> getCarDetail(@PathVariable UUID id) {
        CarCustomerDto car = customerCarService.getCarDetail(id);
        return ResponseEntity.ok(car);
    }

}

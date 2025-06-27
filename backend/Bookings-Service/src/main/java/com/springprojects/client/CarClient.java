package com.springprojects.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "car-service", url = "http://localhost:8081")
public interface CarClient {

    @GetMapping("/api/internal/cars/{id}/price")
    double getPricePerDay(@PathVariable("id") UUID carId);

}

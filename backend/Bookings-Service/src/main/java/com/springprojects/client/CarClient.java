package com.springprojects.client;

import com.springprojects.config.FeignClientConfig;
import com.springprojects.dto.client.CarInformationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "car-service", url = "http://localhost:8081", configuration = FeignClientConfig.class)
public interface CarClient {

    @GetMapping("/api/internal/cars/{id}/information")
    CarInformationDto getCarInformation(@PathVariable("id") UUID carId);

}

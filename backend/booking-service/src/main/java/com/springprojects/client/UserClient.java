package com.springprojects.client;

import com.springprojects.config.FeignClientConfig;
import com.springprojects.dto.client.CustomerInformationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/api/internal/user/{id}/information")
    CustomerInformationDto getCustomerInformation(@PathVariable("id") UUID userId);

}

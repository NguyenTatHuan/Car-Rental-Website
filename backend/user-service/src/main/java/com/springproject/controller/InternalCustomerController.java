package com.springproject.controller;

import com.springproject.dto.CustomerInformationDto;
import com.springproject.entity.UserInformation;
import com.springproject.repository.UserInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/internal/user")
@RequiredArgsConstructor
public class InternalCustomerController {

    private final UserInformationRepository userInformationRepository;

    @GetMapping("/{id}/information")
    public CustomerInformationDto getCustomerInformation(@PathVariable UUID id) {
        UserInformation userInformation = userInformationRepository.findByUserId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User info not found with id: " + id));

        return CustomerInformationDto.builder()
                .fullName(userInformation.getFullName())
                .email(userInformation.getEmail())
                .build();
    }

}

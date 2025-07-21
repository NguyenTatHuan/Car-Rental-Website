package com.springproject.controller;

import com.springproject.dto.userinformation.UserInformationUpdateDto;
import com.springproject.service.customer.userinformation.CustomerUserInformationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/user-information")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerUserInformationController {

    private final CustomerUserInformationService customerUserInformationService;

    @GetMapping
    public ResponseEntity<UserInformationUpdateDto> getMyInformation() {
        return ResponseEntity.ok(customerUserInformationService.getMyInformation());
    }

    @PutMapping
    public ResponseEntity<UserInformationUpdateDto> updateMyInformation(
            @Valid @RequestBody UserInformationUpdateDto dto
    ) {
        return ResponseEntity.ok(customerUserInformationService.updateMyInformation(dto));
    }

}

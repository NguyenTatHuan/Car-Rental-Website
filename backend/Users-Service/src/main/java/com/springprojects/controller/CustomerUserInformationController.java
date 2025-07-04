package com.springprojects.controller;

import com.springprojects.dto.userInformation.UserInformationUpdateDto;
import com.springprojects.entity.User;
import com.springprojects.services.customer.userInformation.CustomerUserInformationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/user-information")
@RequiredArgsConstructor
public class CustomerUserInformationController {

    private final CustomerUserInformationService customerUserInformationService;

    @GetMapping
    public ResponseEntity<UserInformationUpdateDto> getMyInformation(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();
        return ResponseEntity.ok(customerUserInformationService.getMyInformation(userId));
    }

    @PutMapping
    public ResponseEntity<UserInformationUpdateDto> updateMyInformation(
            @Valid @RequestBody UserInformationUpdateDto dto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();
        return ResponseEntity.ok(customerUserInformationService.updateMyInformation(userId, dto));
    }

}

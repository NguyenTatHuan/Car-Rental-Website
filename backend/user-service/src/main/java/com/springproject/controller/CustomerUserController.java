package com.springproject.controller;

import com.springproject.dto.user.ChangePasswordDto;
import com.springproject.service.customer.user.CustomerUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerUserController {

    private final CustomerUserService customerUserService;

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        customerUserService.changePassword(dto);
        return ResponseEntity.ok("Password changed successfully!");
    }

}

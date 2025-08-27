package com.springproject.controller;

import com.springproject.dto.user.ChangePasswordDto;
import com.springproject.service.customer.user.CustomerUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerUserController {

    private final CustomerUserService customerUserService;

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestAttribute("userId") UUID userId,
            @Valid @RequestBody ChangePasswordDto dto
    ) {
        customerUserService.changePassword(userId, dto);
        return ResponseEntity.ok("Password changed successfully!");
    }

}

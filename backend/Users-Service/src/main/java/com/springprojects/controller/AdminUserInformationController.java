package com.springprojects.controller;

import com.springprojects.dto.userInformation.UserInformationDto;
import com.springprojects.dto.userInformation.UserInformationUpdateDto;
import com.springprojects.services.admin.userInformation.AdminUserInformationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/user-information")
@RequiredArgsConstructor
public class AdminUserInformationController {

    private final AdminUserInformationService adminUserInformationService;

    @GetMapping
    public ResponseEntity<List<UserInformationDto>> getAllUserInformation() {
        List<UserInformationDto> result = adminUserInformationService.getAllUserInformation();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInformationDto> getUserInformationById(@PathVariable UUID id) {
        UserInformationDto dto = adminUserInformationService.getUserInformationById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInformationDto> updateUserInformation(
            @PathVariable UUID id,
            @Valid @RequestBody UserInformationUpdateDto dto
    ) {
        UserInformationDto updated = adminUserInformationService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserInformation(@PathVariable UUID id) {
        adminUserInformationService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}

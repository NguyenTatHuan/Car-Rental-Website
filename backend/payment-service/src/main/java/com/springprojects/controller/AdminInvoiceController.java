package com.springprojects.controller;

import com.springprojects.dto.invoice.InvoiceCreateRequest;
import com.springprojects.dto.invoice.InvoiceResponse;
import com.springprojects.dto.invoice.InvoiceUpdateRequest;
import com.springprojects.service.admin.invoice.AdminInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/invoice")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminInvoiceController {

    private final AdminInvoiceService adminInvoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(adminInvoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminInvoiceService.getInvoiceById(id));
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceCreateRequest request) {
        InvoiceResponse created = adminInvoiceService.createInvoice(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(
            @PathVariable UUID id,
            @Valid @RequestBody InvoiceUpdateRequest request) {
        InvoiceResponse updated = adminInvoiceService.updateInvoice(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) {
        adminInvoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

}

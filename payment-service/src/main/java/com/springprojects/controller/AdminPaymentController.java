package com.springprojects.controller;

import com.springprojects.dto.payment.PaymentResponse;
import com.springprojects.enums.PaymentMethod;
import com.springprojects.enums.PaymentStatus;
import com.springprojects.service.admin.payment.AdminPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/payment")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(adminPaymentService.getAllPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(adminPaymentService.getPaymentById(paymentId));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable UUID paymentId) {
        adminPaymentService.deletePayment(paymentId);
        return ResponseEntity.ok("Payment deleted successfully!");
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<String> updatePaymentStatus(
            @PathVariable UUID paymentId,
            @RequestParam PaymentStatus status) {
        adminPaymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok("Payment status updated successfully!");
    }

    @PutMapping("/{paymentId}/method")
    public ResponseEntity<String> updatePaymentMethod(
            @PathVariable UUID paymentId,
            @RequestParam PaymentMethod method) {
        adminPaymentService.updatePaymentMethod(paymentId, method);
        return ResponseEntity.ok("Payment method updated successfully!");
    }

}

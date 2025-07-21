package com.springprojects.controller;

import com.springprojects.service.stripe.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final StripeService stripeService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            stripeService.handleWebhook(payload, sigHeader);
            return ResponseEntity.ok("Webhook processed successfully!");
        } catch (StripeException e) {
            return ResponseEntity.status(400).body("StripeException: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error!");
        }

    }

}

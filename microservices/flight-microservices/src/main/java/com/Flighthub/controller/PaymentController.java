package com.airwise.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController { // ✅ FIXED: Removed the invalid hyphen

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> makePayment(@RequestBody PaymentRequest request) {
        // Log payment request (you can persist this to DB)
        System.out.println("Received payment for: " + request.getAmount() + " using " + request.getCard());

        // Return mock success response
        PaymentResponse response = new PaymentResponse();
        response.setId(UUID.randomUUID().toString());
        response.setMessage("Payment successful");

        return ResponseEntity.ok(response);
    }

    // DTO for incoming payment request
    @Data
    static class PaymentRequest {
        private double amount;
        private String card;
        private String promoCode;
    }

    // DTO for outgoing response
    @Data
    static class PaymentResponse {
        private String id;
        private String message;
    }
}

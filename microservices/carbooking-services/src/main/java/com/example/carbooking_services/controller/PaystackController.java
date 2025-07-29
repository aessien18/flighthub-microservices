package com.example.carbooking_services.controller;

import com.example.carbooking_services.service.PaystackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paystack")
public class PaystackController {

    @Autowired
    private PaystackService paystackService;

    @PostMapping("/initiate")
    public ResponseEntity<String> initiate(@RequestParam String email, @RequestParam double amount) {
        return paystackService.initializePayment(email, amount);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String reference) {
        return paystackService.verifyPayment(reference);
    }
}

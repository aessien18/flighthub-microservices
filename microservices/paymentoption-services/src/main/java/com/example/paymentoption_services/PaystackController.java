package com.example.paymentoption_services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paystack")
public class PaystackController {

    @Autowired
    private PaystackService paystackService;

    @GetMapping("/verify")
    public String verifyPayment(@RequestParam String reference) {
        return paystackService.verifyTransaction(reference);
    }

    @PostMapping("/initialize")
    public PaystackInitResponse initializePayment(@RequestBody PaystackInitRequest request) {
        return paystackService.initializeTransaction(request);
    }
}

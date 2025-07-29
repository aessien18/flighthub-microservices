package com.example.carbooking_services.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PaystackService {

    @Value("${paystack.secret.key}")
    private String secretKey;

    private final String INITIATE_URL = "https://api.paystack.co/transaction/initialize";
    private final String VERIFY_URL = "https://api.paystack.co/transaction/verify/";

    public ResponseEntity<String> initializePayment(String email, double amount) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("amount", (int)(amount * 100)); // Paystack accepts kobo/pesewas

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(INITIATE_URL, entity, String.class);
    }

    public ResponseEntity<String> verifyPayment(String reference) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                VERIFY_URL + reference,
                HttpMethod.GET,
                entity,
                String.class
        );
    }
}

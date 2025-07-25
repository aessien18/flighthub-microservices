package com.example.paymentoption_services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaystackService {

    @Value("${paystack.secret.key}")
    private String paystackSecretKey;

    public String verifyTransaction(String reference) {
        String url = "https://api.paystack.co/transaction/verify/" + reference;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + paystackSecretKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
    public PaystackInitResponse initializeTransaction(PaystackInitRequest request) {
        String url = "https://api.paystack.co/transaction/initialize";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + paystackSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("email", request.getEmail());
        body.put("amount", request.getAmount());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.path("data");
            PaystackInitResponse result = new PaystackInitResponse();
            result.setAuthorizationUrl(data.path("authorization_url").asText());
            result.setAccessCode(data.path("access_code").asText());
            result.setReference(data.path("reference").asText());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Paystack response", e);
        }
    }
}

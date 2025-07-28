package com.example.pushnotification_services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tokens")
public class PushNotificationController {

    private final FirebaseMessagingService messagingService;
    private final List<String> tokens = new ArrayList<>();

    public PushNotificationController(FirebaseMessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping
    public ResponseEntity<String> registerToken(@RequestBody DeviceToken token) {
        tokens.add(token.getToken());
        return ResponseEntity.ok("Token registered");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody FlightNotificationRequest request) {
        for (String token : tokens) {
            messagingService.sendFlightNotification(token, request);
        }
        return ResponseEntity.ok("Flight notifications sent");
    }
}

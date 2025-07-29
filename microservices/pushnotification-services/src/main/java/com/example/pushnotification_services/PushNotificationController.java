package com.example.pushnotification_services;

import com.example.pushnotification_services.service.InMemoryDeviceTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tokens")
public class PushNotificationController {

    private final FirebaseMessagingService messagingService;
    private final InMemoryDeviceTokenService deviceTokenService;

    public PushNotificationController(FirebaseMessagingService messagingService, InMemoryDeviceTokenService deviceTokenService) {
        this.messagingService = messagingService;
        this.deviceTokenService = deviceTokenService;
    }

    // Register a device token
    @PostMapping
    public ResponseEntity<Map<String, String>> registerToken(@RequestBody DeviceToken deviceToken) {
        Map<String, String> response = new HashMap<>();
        if (deviceTokenService.findByToken(deviceToken.getToken()) == null) {
            deviceTokenService.save(deviceToken);
            response.put("message", "Token registered successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Token already registered");
            return ResponseEntity.ok(response);
        }
    }

    // Send to one specific device
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendToOne(@RequestBody FlightNotificationRequest request) {
        Map<String, String> response = new HashMap<>();
        if (request.getToken() == null || request.getToken().isEmpty()) {
            response.put("message", "Target device token is required.");
            return ResponseEntity.badRequest().body(response);
        }
        messagingService.sendFlightNotification(request.getToken(), request);
        response.put("message", "Notification sent to one device.");
        return ResponseEntity.ok(response);
    }

    // Broadcast to all registered devices
    @PostMapping("/sendAll")
    public ResponseEntity<Map<String, String>> sendToAll(@RequestBody FlightNotificationRequest request) {
        List<DeviceToken> deviceTokens = deviceTokenService.findAll();
        Map<String, String> response = new HashMap<>();
        if (deviceTokens.isEmpty()) {
            response.put("message", "No device tokens registered.");
            return ResponseEntity.ok(response);
        }
        for (DeviceToken deviceToken : deviceTokens) {
            messagingService.sendFlightNotification(deviceToken.getToken(), request);
        }
        response.put("message", "Notification sent to all devices.");
        return ResponseEntity.ok(response);
    }
}

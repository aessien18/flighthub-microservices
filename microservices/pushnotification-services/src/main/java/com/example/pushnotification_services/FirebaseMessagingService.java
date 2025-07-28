package com.example.pushnotification_services;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseMessagingService {

    public void sendFlightNotification(String token, FlightNotificationRequest request) {
        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("flightNumber", request.getFlightNumber());
        data.put("gate", request.getGate());
        data.put("status", request.getStatus());
        data.put("time", request.getTime());

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(data)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}

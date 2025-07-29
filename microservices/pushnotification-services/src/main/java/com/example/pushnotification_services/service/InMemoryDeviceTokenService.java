package com.example.pushnotification_services.service;

import com.example.pushnotification_services.DeviceToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryDeviceTokenService {
    private final ConcurrentHashMap<String, DeviceToken> deviceTokens = new ConcurrentHashMap<>();

    public void save(DeviceToken deviceToken) {
        deviceTokens.put(deviceToken.getToken(), deviceToken);
    }

    public DeviceToken findByToken(String token) {
        return deviceTokens.get(token);
    }

    public List<DeviceToken> findAll() {
        return new ArrayList<>(deviceTokens.values());
    }
}
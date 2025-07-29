package com.example.pushnotification_services.repository;

import com.example.pushnotification_services.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    DeviceToken findByToken(String token);
} 
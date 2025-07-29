package com.example.pushnotification_services;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "device_tokens")
@Data
public class DeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String token;
}

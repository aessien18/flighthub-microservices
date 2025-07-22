package com.example.user_services.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

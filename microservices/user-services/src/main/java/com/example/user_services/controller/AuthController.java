package com.example.user_services.controller;

import com.example.user_services.services.AuthService;
import com.example.user_services.dto.AuthRequest;
import com.example.user_services.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin // allow frontend connection
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) throws Exception {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) throws Exception {
        return authService.login(request);
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody String refreshToken) throws Exception {
        return authService.refreshAccessToken(refreshToken);
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) throws Exception {
        return authService.verifyEmail(token);
    }

    @PostMapping("/request-password-reset")
    public String requestPasswordReset(@RequestBody String email) throws Exception {
        return authService.requestPasswordReset(email);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) throws Exception {
        return authService.resetPassword(token, newPassword);
    }
}

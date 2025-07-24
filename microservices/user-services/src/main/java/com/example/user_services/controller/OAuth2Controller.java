package com.example.user_services.controller;

import com.example.user_services.dto.AuthResponse;
import com.example.user_services.model.User;
import com.example.user_services.repository.UserRepository;
import com.example.user_services.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/oauth2")
public class OAuth2Controller {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/success")
    public AuthResponse oauth2Success(Authentication authentication) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        // Find or create user
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(name != null ? name.split(" ")[0] : "");
            newUser.setLastName(name != null && name.split(" ").length > 1 ? name.split(" ")[1] : "");
            newUser.setUsername(email.split("@")[0]);
            newUser.setEnabled(true);
            return userRepository.save(newUser);
        });

        String token = jwtUtil.generateToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthResponse(token, refreshToken, user.getUsername(), user.getEmail(), user.getRole().name());
    }
}

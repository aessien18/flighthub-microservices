package com.example.user_services.services;

import com.example.user_services.dto.AuthRequest;
import com.example.user_services.dto.AuthResponse;
import com.example.user_services.model.User;
import com.example.user_services.repository.UserRepository;
import com.example.user_services.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse register(AuthRequest request) throws Exception {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new Exception("User already exists");
        }

        String uniqueUsername = generateUniqueUsername(request.getFirstName(), request.getLastName());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(uniqueUsername);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setEnabled(false);

        userRepository.save(user);

        // Simulate sending email
        System.out.println("Verification email sent to " + user.getEmail() + ": http://localhost:8081/api/auth/verify?token=" + verificationToken);

        // Do not return tokens until verified
        return new AuthResponse(null, null, user.getUsername(), user.getEmail(), user.getRole().name());
    }

    public String verifyEmail(String token) throws Exception {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);
        if (userOpt.isEmpty()) {
            throw new Exception("Invalid verification token");
        }
        User user = userOpt.get();
        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return "Email verified successfully!";
    }

    public AuthResponse login(AuthRequest request) throws Exception {
        Optional<User> userOpt;
        // Check if the input is an email (contains '@') or username
        if (request.getEmail() != null && request.getEmail().contains("@")) {
            userOpt = userRepository.findByEmail(request.getEmail());
        } else {
            userOpt = userRepository.findByUsername(request.getEmail()); // treat 'email' field as username if not an email
        }
        if (userOpt.isEmpty()) {
            throw new Exception("Invalid email/username or password");
        }
        User user = userOpt.get();
        if (!user.isEnabled()) {
            throw new Exception("Please verify your email before logging in.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Invalid email/username or password");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return new AuthResponse(token, refreshToken, user.getUsername(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse refreshAccessToken(String refreshToken) throws Exception {
        Optional<User> userOpt = userRepository.findByRefreshToken(refreshToken);
        if (userOpt.isEmpty()) {
            throw new Exception("Invalid refresh token");
        }
        User user = userOpt.get();
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new Exception("Refresh token expired");
        }
        String newAccessToken = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(newAccessToken, refreshToken, user.getUsername(), user.getEmail(), user.getRole().name());
    }

    public String requestPasswordReset(String email) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new Exception("User not found");
        }
        User user = userOpt.get();
        String resetToken = UUID.randomUUID().toString();
        user.setVerificationToken(resetToken); // reuse verificationToken field for reset
        userRepository.save(user);
        // Simulate sending email
        System.out.println("Password reset email sent to " + user.getEmail() + ": http://localhost:8081/api/auth/reset-password?token=" + resetToken);
        return "Password reset email sent!";
    }

    public String resetPassword(String token, String newPassword) throws Exception {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);
        if (userOpt.isEmpty()) {
            throw new Exception("Invalid or expired reset token");
        }
        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationToken(null);
        userRepository.save(user);
        return "Password reset successful!";
    }

   private String generateUniqueUsername(String firstName, String lastName) {
    String baseUsername;
    int totalLength = firstName.length() + lastName.length();
    if (totalLength <= 12) {
        baseUsername = firstName.substring(0, Math.min(2, firstName.length())).toLowerCase() + lastName.toLowerCase();
    } else {
        baseUsername = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
    }

    String username = baseUsername;
    int counter = 1;
    while (userRepository.findByUsername(username).isPresent()) {
        username = baseUsername + counter++;
    }
    return username;
}

}

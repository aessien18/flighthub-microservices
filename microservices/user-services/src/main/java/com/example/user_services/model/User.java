package com.example.user_services.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(length = 500)
    private String refreshToken;  // New Field

    private boolean enabled = false;  // For email verification
    private String verificationToken; // Email verification token

    // ID from the OAuth2 provider
}

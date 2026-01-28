package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.auth.AuthRequest;
import com.trandnquang.roomie.dto.auth.AuthResponse;
import com.trandnquang.roomie.dto.auth.RegisterRequest;
import com.trandnquang.roomie.entity.User;
import com.trandnquang.roomie.model.enums.UserRole;
import com.trandnquang.roomie.model.enums.UserStatus;
import com.trandnquang.roomie.repo.UserRepository;
import com.trandnquang.roomie.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // --- 1. REGISTER ---
    @Transactional
    public void register(RegisterRequest request) {
        // Validation: Throw IllegalArgumentException (Mapped to 400 Bad Request usually)
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Determine Role: Default to MANAGER if not provided
        UserRole roleToSet = (request.getRole() != null) ? request.getRole() : UserRole.ROLE_MANAGER;

        // Create Entity
        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleToSet)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(newUser);
    }

    // --- 2. LOGIN ---
    public AuthResponse login(AuthRequest request) {
        try {
            // 1. Authenticate via Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // 2. Fetch User
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 3. Generate Token
        // Lưu ý: Generate token dựa trên Username (như file JwtUtils cũ của bạn)
        String token = jwtUtils.generateToken(user.getUsername());

        // 4. Return DTO with ID (Important for Frontend)
        return AuthResponse.builder()
                .token(token)
                .id(user.getId()) // QUAN TRỌNG: Thêm ID
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
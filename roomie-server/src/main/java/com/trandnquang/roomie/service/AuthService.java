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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    // --- 1. REGISTER LOGIC ---
    @Transactional
    public void register(RegisterRequest request) {
        // Validation Logic
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // [MANUAL MAPPING] DTO -> Entity
        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword())) // Hash pass
                .role(UserRole.ROLE_MANAGER) // Mặc định là Manager
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(newUser);
    }

    // --- 2. LOGIN LOGIC ---
    public AuthResponse login(AuthRequest request) {
        // 1. Xác thực username/password (Spring Security tự làm việc này)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Nếu vượt qua bước trên -> Tìm user để lấy thông tin
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Sinh Token
        String token = jwtUtils.generateToken(user.getUsername());

        // [MANUAL MAPPING] Entity -> Response DTO
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
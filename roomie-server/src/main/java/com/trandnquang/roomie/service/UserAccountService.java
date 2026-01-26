package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.request.LoginRequest;
import com.trandnquang.roomie.dto.response.AuthResponse;
import com.trandnquang.roomie.entity.UserAccount;
import com.trandnquang.roomie.repo.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public AuthResponse login(LoginRequest request) {
        // 1. Tìm user
        Optional<UserAccount> userOpt = userAccountRepository.findByUsername(request.getUsername());

        // 2. Check password (Tạm thời so sánh plain text, sau này dùng BCrypt)
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            UserAccount user = userOpt.get();

            // 3. Map sang Response
            return AuthResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .role("ADMIN") // Mặc định role
                    .token("dummy-token-jwt-123456") // Giả lập token
                    .build();
        }
        throw new RuntimeException("Invalid username or password");
    }
}
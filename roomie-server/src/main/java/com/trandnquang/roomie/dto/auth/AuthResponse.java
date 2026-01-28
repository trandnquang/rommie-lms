package com.trandnquang.roomie.dto.auth;

import com.trandnquang.roomie.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer"; // Standard JWT Type
    private Long id;                // Frontend thường cần ID để fetch profile
    private String username;
    private String fullName;
    private String email;
    private UserRole role;
    private String avatarUrl;
}
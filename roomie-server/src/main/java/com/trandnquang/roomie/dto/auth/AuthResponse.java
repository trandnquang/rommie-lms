package com.trandnquang.roomie.dto.auth;

import com.trandnquang.roomie.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;      // JWT Token
    private String username;
    private String fullName;
    private String email;
    private UserRole role;     // Để Frontend phân quyền menu
    private String avatarUrl;
}
package com.trandnquang.roomie.dto.auth;

import com.trandnquang.roomie.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    // Optional: Yêu cầu password phức tạp (Chữ hoa, số, ký tự đặc biệt)
    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$", message = "Password must contain uppercase, lowercase and number")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    // Mặc định là null (Service sẽ set là TENANT hoặc MANAGER).
    // Nhưng nếu Admin tạo user thì cần field này.
    private UserRole role;
}
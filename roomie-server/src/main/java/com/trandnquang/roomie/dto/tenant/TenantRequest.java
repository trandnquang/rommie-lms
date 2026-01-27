package com.trandnquang.roomie.dto.tenant;

import com.trandnquang.roomie.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TenantRequest {
    @NotBlank(message = "ID Card is required")
    private String identityCardNumber;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10,11}$", message = "Phone number must be 10-11 digits")
    private String phoneNumber;

    private String email;
    private LocalDate birthday;
    private Gender gender;

    // Address Info
    private String addressLine;
    private String ward;
    private String district;
    private String city;
}
package com.trandnquang.roomie.dto.response;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TenantResponse {
    private Long id;
    private String identityNumber;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String addressFull; // Có thể gộp addressLine + ward + district + city
    private String gender;
    private LocalDate dateOfBirth;
}
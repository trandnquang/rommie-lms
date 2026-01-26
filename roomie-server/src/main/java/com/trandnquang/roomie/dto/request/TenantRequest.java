package com.trandnquang.roomie.dto.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TenantRequest {
    private String identityNumber; // CCCD
    private String fullName;
    private String phoneNumber;
    private String email;
    private String addressLine;
    private String ward;
    private String district;
    private String city;
    private String gender;
    private LocalDate dateOfBirth;
}
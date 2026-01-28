package com.trandnquang.roomie.dto.resident;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidentResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String identityCardNumber;
    private boolean isContractHolder;
    private LocalDate moveInDate;
}
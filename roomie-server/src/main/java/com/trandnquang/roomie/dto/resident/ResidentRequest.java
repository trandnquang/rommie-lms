package com.trandnquang.roomie.dto.resident;

import lombok.Data;

@Data
public class ResidentRequest {
    private String fullName;
    private String phoneNumber;
    private String identityCardNumber;
    private boolean isContractHolder; // Đánh dấu nếu là người ký HĐ
}
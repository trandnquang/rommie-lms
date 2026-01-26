package com.trandnquang.roomie.dto.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ResidentRequest {
    private Long contractId;
    private Long tenantId; // ID của người thuê (Tenant) được thêm vào làm cư dân
    private String role;   // LEADER / MEMBER
    private LocalDate moveInDate;
}
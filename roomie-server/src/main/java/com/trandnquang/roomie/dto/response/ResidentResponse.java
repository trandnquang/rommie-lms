package com.trandnquang.roomie.dto.response;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ResidentResponse {
    private Long id;
    private String tenantName;
    private String tenantPhone;
    private String role;
    private LocalDate moveInDate;
}
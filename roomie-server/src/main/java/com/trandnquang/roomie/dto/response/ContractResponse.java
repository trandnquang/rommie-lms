package com.trandnquang.roomie.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractResponse {
    private Long id;
    private String roomNumber;    // Flatten data
    private String propertyName;  // Flatten data
    private String tenantName;    // Flatten data
    private String tenantPhone;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal deposit;
    private BigDecimal rentPrice;
    private String status;
}
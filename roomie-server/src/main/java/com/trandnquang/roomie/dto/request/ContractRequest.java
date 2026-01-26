package com.trandnquang.roomie.dto.request;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractRequest {
    private Long roomId;
    private Long tenantId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal deposit;
    private BigDecimal rentPrice; // Giá chốt
    private Integer occupantCount;
}
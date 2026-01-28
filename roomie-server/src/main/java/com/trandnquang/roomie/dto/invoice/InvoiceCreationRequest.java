package com.trandnquang.roomie.dto.invoice;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InvoiceCreationRequest {

    @NotNull(message = "Contract ID is required")
    private Long contractId;

    @NotNull
    private Integer month;

    @NotNull
    private Integer year;

    // Danh sách chỉ số mới (Chỉ dành cho điện/nước có công tơ mét)
    private List<UtilityReading> readings;

    @Data
    public static class UtilityReading {
        private Long utilityId;
        private BigDecimal newIndex; // Chỉ số mới
    }
}
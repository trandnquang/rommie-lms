package com.trandnquang.roomie.dto.invoice;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyUsageInput {
    @NotNull(message = "Contract ID is required")
    private Long contractId;

    @NotNull(message = "Month is required")
    private Integer month;

    @NotNull(message = "Year is required")
    private Integer year;

    // Sử dụng class đã tách ở trên
    private List<ServiceUsageDTO> readings;
}
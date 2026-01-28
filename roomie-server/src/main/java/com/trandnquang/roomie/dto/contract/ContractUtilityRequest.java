package com.trandnquang.roomie.dto.contract;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractUtilityRequest {
    @NotNull(message = "Utility ID is required")
    private Long utilityId;

    @PositiveOrZero(message = "Amount must be >= 0")
    private Integer amount = 1;

    @PositiveOrZero(message = "Start index must be >= 0")
    private BigDecimal startIndex; // Nullable (chỉ bắt buộc với điện/nước)
}
package com.trandnquang.roomie.dto.utility;

import com.trandnquang.roomie.model.enums.PricingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UtilityRequest {
    @NotBlank(message = "Utility name is required")
    private String name;

    @NotNull(message = "Pricing type is required")
    private PricingType pricingType; // FLAT, TIERED...

    private String unit; // kWh, m3...

    private BigDecimal basePrice; // Giá cơ bản (nếu là FLAT)

    private String description;

    // Nhận List object từ FE, Backend sẽ convert sang JSON String để lưu
    private List<TierConfig> tierConfig;

    private boolean isActive;
}
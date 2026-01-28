package com.trandnquang.roomie.dto.contract;

import com.trandnquang.roomie.dto.utility.TierConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractUtilityResponse {
    private Long id;
    private Long utilityId;
    private String utilityName;
    private String utilityUnit;

    // Giá chốt (cho Fixed/Flat)
    private BigDecimal price;

    // Cấu hình bậc thang chốt (cho Tiered) - QUAN TRỌNG
    private List<TierConfig> tierConfig;

    private Integer amount;
    private BigDecimal startIndex;
}
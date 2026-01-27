package com.trandnquang.roomie.dto.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractUtilityResponse {
    private Long id;              // ID của dòng ContractUtility
    private Long utilityId;       // ID của Utility gốc
    private String utilityName;   // Tên dịch vụ (Flatten từ Utility gốc)
    private String utilityUnit;   // Đơn vị tính (kWh, Người...)
    private BigDecimal price;     // Giá áp dụng
    private Integer amount;       // Số lượng đăng ký
    private BigDecimal startIndex;    // Chỉ số ban đầu (nếu có)
}
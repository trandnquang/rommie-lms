package com.trandnquang.roomie.dto.invoice;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class InvoiceDetailResponse {
    private String serviceName;
    private BigDecimal oldIndex;
    private BigDecimal newIndex;
    private BigDecimal usageAmount; // Số lượng tiêu thụ
    private BigDecimal appliedPrice; // Đơn giá lúc tính
    private BigDecimal totalPrice;  // Thành tiền
    private String description;
}